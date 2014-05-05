package com.healthslife.healthtest;
import static com.googlecode.javacv.cpp.opencv_core.*; 
import static com.googlecode.javacv.cpp.opencv_imgproc.*; 
import static com.googlecode.javacv.cpp.opencv_highgui.*;

import java.io.UnsupportedEncodingException;

import com.googlecode.javacpp.BytePointer;
import com.googlecode.javacv.cpp.opencv_core.CvMat;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

import java.util.Queue;  
import java.util.LinkedList; 

public class ECGAnalysis {
	final static double tx = 7*0.2/960;
	/**
	 * @param args
	 */
	
	public static void GrayTransform(IplImage ColorImage, int gray[][]) {
		for (int i = 0; i < ColorImage.height(); i++) {
			for (int j = 0; j < ColorImage.width(); j++) {
				ColorImage.imageData();
				int B = (int)ColorImage.imageData().get(i*ColorImage.widthStep() + j*3) & 0xFF;     //B����  
				int G = (int)ColorImage.imageData().get(i*ColorImage.widthStep() + j*3 + 1) & 0xFF; //G����  
				int R = (int)ColorImage.imageData().get(i*ColorImage.widthStep() + j*3 + 2) & 0xFF; //R���� 
				int V = Math.max(Math.max(B, G), R);
				//gray[i][j] = (114*B+587*255+229*R)/1000;
				gray[i][j] = V;
			}
		}
	}
	public static void BinarizationPrepare(int n, int m, int gray[][]) {
		int avg[] = new int[m];
		int min = 255;
		for (int i = 0; i < m; i++) {
			avg[i] = 0;
			for (int j = 0; j < n; j++) {
				avg[i] += gray[j][i];
			}
			avg[i] /= n;
			min = Math.min(min, avg[i]);
		}
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				if (gray[i][j] > min) {
					gray[i][j] = 255;
				}
			}
		}
	}

	public static void Binarization(int n, int m, int gray[][]) {
		int T = 0, TT = 0;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				TT += gray[i][j];
			}
		}
		TT /= n*m;
		while (T != TT) {
			T = TT;
			int m1 = 0, m2 = 0, sum1 = 0, sum2 = 0;
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < m; j++) {
					if (gray[i][j] > T) {
						m2 += gray[i][j];
						sum2++;
					} else {
						m1 += gray[i][j];
						sum1++;
					}
				}
			}
			if (m1 != 0) {
				m1 /= sum1;
			}
			if (m2 != 0) {
				m2 /= sum2;
			}
			TT = (m1+m2)/2;
		}
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				if (gray[i][j] > T) {
					gray[i][j] = 255;
				} else {
					gray[i][j] = 0;
				}
			}
		}
	}
	public static void OutlierRemoval(int n, int m, int gray[][]) {
		int g[][] = new int[n][m];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				g[i][j] = gray[i][j];
				if (j < m/5) {
					g[i][j] = 255;
				}
				if (j > m/5*4) {
					g[i][j] = 255;
				}
				if (g[i][j] == 255) {
					continue;
				}
				int sum = 0;
				for (int dx = -2; dx <= 2; dx++) {
					for (int dy = -2; dy <= 2; dy++) {
						int x = i+dx;
						int y = j+dy;
						if (x >= 0 && x < n && y >= 0 && y < m) {
							if (gray[x][y] == 0) {
								sum++;
							}
						}
					}
				}
				if (sum < 13) {
					g[i][j] = 255;
				}
			}
		}
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				gray[i][j] = g[i][j];
			}
		}
	}
	
	public static boolean checkPDouble(int p1, int p2, int h[]) {
		int x = p1+1;
		while (h[x] >= h[x-1]) {
			x++;
		}
		int y = x+1;
		while (h[y] <= h[y-1]) {
			y++;
		}
		for (int i = y; i <= p2; i++) {
			if (h[i] > h[y]) {
				y = i;
			}
		}
		if (y-x > 2 && h[x] > h[p2]+1 && h[y] > h[p2]+1 && h[x] > h[p1]+1 && h[y] > h[p1]+1) {
			return true;
		}
		return false;
	}
	public static String Analysis(String file) {
		//��ȡͼƬ  
		IplImage ColorImage = cvLoadImage(file);  
		if (ColorImage == null) {
			System.out.println("FUCK");
			System.exit(1);
		}
		IplImage GrayImage = cvCreateImage(cvGetSize(ColorImage), 8, 1);
		CvMat GrayMat = GrayImage.asCvMat();
		int n = GrayImage.height(), m = GrayImage.width();
		int gray[][] = new int[Math.max(n, m)][Math.max(n, m)];

		//�Ҷȱ任
		GrayTransform(ColorImage, gray);
		boolean rotate = false;
		if (n < m) {
			rotate = true;
			Rotate(n, m, gray);
			int tmp = n;
			n = m;
			m = tmp;
		}
		//��ֵ��
		BinarizationPrepare(n, m, gray);
		Binarization(n, m, gray);
		//ȥ��������
		for (int run = 0; run < 15; run++) {
			OutlierRemoval(n, m, gray);
		}
		//��������
		String res = "�쳣���:\n";
		int h[] = new int[n];
		for (int i = 0; i < n; i++) {
			int sum = 0;
			h[i] = 0;
			for (int j = 0; j < m; j++) {
				if (gray[i][j] == 0) {
					h[i] += j;
					sum++;
				}
			}
			if (sum != 0) {
				h[i] /= sum;
			}
		}
		//�ĵ�ͼ����ڵ�
		int p1 = 0;
		int min = m;
		for (int i = 0; i < n; i++) {
			if (h[i]-min > 5) break;
			p1 = i;
			if (h[i] != 0) {
				min = Math.min(min, h[i]);
			}
		}
		int p2 = p1+1;
		while (h[p2]-min > 5) {
			p2++;
		}
		//�ж��Ƿ�˫��
		if (checkPDouble(p1, p2, h)) {
			res += "����꼲��,��Ѫѹ��,����˥�ߣ����ҷʴ�\n";
		}
		//�ж�PRʱ�� ����Ϊ0.12~0.2s
		double PR = (p2-p1)*tx;
		if (PR < 0.12) {
			res += "��ʪ��,�ļ���,����\n";
		}
		if (PR > 0.2) {
			res += "Ԥ���ۺ�֢,�����񾭹���֢\n";
		}
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				gray[i][j] = 255;
			}
		}
		int s = p2;
		for (int i = p2; i < p2+300; i++) {
			if (h[i] != 0 && h[i] < h[s]) {
				s = i;
			}
		}
		int J = s;
		for (int i = J; i < n; i++) {
			if (h[i+5] <= h[i] && h[i+5] != 0) {
				break;
			}
			J = i;
		}
		double QRS = (J-p2)*tx;
		if (QRS > 0.1) {
			res += "���ҷʴ�,��֧����,Ԥ���ۺ�֢,����\n";
		}
		for (int i = p1; i < J; i++) {
			gray[i][h[i]] = 0;
		}
		return res;
	}
	public static void Rotate(int n, int m, int gray[][]) {
		int g[][] = new int[m][n];
		for (int j = 0; j < n; j++) {
			for (int i = 0; i < m; i++) {
				g[i][j] = gray[n-1-j][i];
			}
		}
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				gray[i][j] = g[i][j];
			}
		}
	}
}
