import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;

import javax.imageio.ImageIO;

public class xx {
	/*
     * ͼƬ����,w��hΪ���ŵ�Ŀ���Ⱥ͸߶�
     * srcΪԴ�ļ�Ŀ¼��destΪ���ź󱣴�Ŀ¼
     */
	 public static void zoomImage(String src,String dest,int w,int h) throws Exception {
	        
	        double wr=0,hr=0;
	        File srcFile = new File(src);
	        File destFile = new File(dest);

	        BufferedImage bufImg = ImageIO.read(srcFile); //��ȡͼƬ
	        Image Itemp = bufImg.getScaledInstance(w, h, bufImg.SCALE_SMOOTH);//��������Ŀ��ͼƬģ��
	        
	        wr=w*1.0/bufImg.getWidth();     //��ȡ���ű���
	        hr=h*1.0 / bufImg.getHeight();

	        AffineTransformOp ato = new AffineTransformOp(AffineTransform.getScaleInstance(wr, hr), null);
	        Itemp = ato.filter(bufImg, null);
	        try {
	            ImageIO.write((BufferedImage) Itemp,dest.substring(dest.lastIndexOf(".")+1), destFile); //д���������ͼƬ
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }
	    }
	  /*
	     * ͼƬ����������
	     * sizeΪ�ļ���С
	     */
	    public static void zoomImage(String src,String dest,Integer size) throws Exception {
	        File srcFile = new File(src);
	        File destFile = new File(dest);
	        
	        long fileSize = srcFile.length();
	        if(fileSize < size * 1024)   //�ļ�����size kʱ���Ž�������,ע�⣺size��KΪ��λ
	            return;
	        
	        Double rate = (size * 1024 * 0.5) / fileSize; // ��ȡ�������ű���
	        
	        BufferedImage bufImg = ImageIO.read(srcFile);
	        Image Itemp = bufImg.getScaledInstance(bufImg.getWidth(), bufImg.getHeight(), bufImg.SCALE_SMOOTH);
	            
	        AffineTransformOp ato = new AffineTransformOp(AffineTransform.getScaleInstance(rate, rate), null);
	        Itemp = ato.filter(bufImg, null);
	        try {
	            ImageIO.write((BufferedImage) Itemp,dest.substring(dest.lastIndexOf(".")+1), destFile);
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }
	    }
	 public static void traverseFolder2(String path) {

	        File file = new File(path);
	        if (file.exists()) {
	            File[] files = file.listFiles();
	            if (files.length == 0) {
	                System.out.println("�ļ����ǿյ�!");
	                return;
	            } else {
	                for (File file2 : files) {
	                    if (file2.isDirectory()) {
	                        System.out.println("�ļ���:" + file2.getAbsolutePath());
	                        traverseFolder2(file2.getAbsolutePath());
	                        try {
								zoomImage(file2.getAbsolutePath(), "C:\\Users\\lenovo\\Downloads", 32, 32);
								System.out.println("ok!");
							} catch (Exception e) {
								e.printStackTrace();
							}
	                    } else {
	                        System.out.println("�ļ�:" + file2.getAbsolutePath());
	                        try {
								zoomImage(file2.getAbsolutePath(), "C:\\Users\\lenovo\\Downloads\\map-marker1\\"+file2.getName(), 32, 32);
								System.out.println("ok222222!");
							} catch (Exception e) {
								e.printStackTrace();
							}
	                    }
	                }
	            }
	        } else {
	            System.out.println("�ļ�������!");
	        }
	    }
	 
	 public static void main(String[] args) {
		 traverseFolder2("C:\\Users\\lenovo\\Downloads\\map-marker");
	}
	 
	 

	    /** 
	     * @param im 
	     *            ԭʼͼ�� 
	     * @param resizeTimes 
	     *            ����,����0.5������Сһ��,0.98�ȵ�double���� 
	     * @return ���ش�����ͼ�� 
	     */  
	    public BufferedImage zoomImage(String src) {  
	          
	        BufferedImage result = null;  
	  
	        try {  
	            File srcfile = new File(src);  
	            if (!srcfile.exists()) {  
	                System.out.println("�ļ�������");  
	                  
	            }  
	            BufferedImage im = ImageIO.read(srcfile);  
	  
	            /* ԭʼͼ��Ŀ�Ⱥ͸߶� */  
	            int width = im.getWidth();  
	            int height = im.getHeight();  
	              
	            //ѹ������  
	            float resizeTimes = 0.3f;  /*���������Ҫת���ɵı���,�����1����ת����1��*/  
	              
	            /* �������ͼƬ�Ŀ�Ⱥ͸߶� */  
	            int toWidth = (int) (width * resizeTimes);  
	            int toHeight = (int) (height * resizeTimes);  
	  
	            /* �����ɽ��ͼƬ */  
	            result = new BufferedImage(toWidth, toHeight,  
	                    BufferedImage.TYPE_INT_RGB);  
	  
	            result.getGraphics().drawImage(  
	                    im.getScaledInstance(toWidth, toHeight,  
	                            java.awt.Image.SCALE_SMOOTH), 0, 0, null);  
	              
	  
	        } catch (Exception e) {  
	            System.out.println("��������ͼ�����쳣" + e.getMessage());  
	        }  
	          
	        return result;  
	  
	    }  
	      
	     public boolean writeHighQuality(BufferedImage im, String fileFullPath) {  
	            try {  
	                /*������ļ���*/  
	                FileOutputStream newimage = new FileOutputStream(fileFullPath);  
	                //JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(newimage);  
	               // JPEGEncodeParam jep = JPEGCodec.getDefaultJPEGEncodeParam(im);  
	                /* ѹ������ */  
	                //jep.setQuality(0.9f, true);  
	               // encoder.encode(im, jep);  
	               /*��JPEG����*/  
	                newimage.close();  
	                return true;  
	            } catch (Exception e) {  
	                return false;  
	            }  
	        }  
	       
	       
}
