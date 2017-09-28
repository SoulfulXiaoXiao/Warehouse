import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;

import javax.imageio.ImageIO;

public class xx {
	/*
     * 图片缩放,w，h为缩放的目标宽度和高度
     * src为源文件目录，dest为缩放后保存目录
     */
	 public static void zoomImage(String src,String dest,int w,int h) throws Exception {
	        
	        double wr=0,hr=0;
	        File srcFile = new File(src);
	        File destFile = new File(dest);

	        BufferedImage bufImg = ImageIO.read(srcFile); //读取图片
	        Image Itemp = bufImg.getScaledInstance(w, h, bufImg.SCALE_SMOOTH);//设置缩放目标图片模板
	        
	        wr=w*1.0/bufImg.getWidth();     //获取缩放比例
	        hr=h*1.0 / bufImg.getHeight();

	        AffineTransformOp ato = new AffineTransformOp(AffineTransform.getScaleInstance(wr, hr), null);
	        Itemp = ato.filter(bufImg, null);
	        try {
	            ImageIO.write((BufferedImage) Itemp,dest.substring(dest.lastIndexOf(".")+1), destFile); //写入缩减后的图片
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }
	    }
	  /*
	     * 图片按比率缩放
	     * size为文件大小
	     */
	    public static void zoomImage(String src,String dest,Integer size) throws Exception {
	        File srcFile = new File(src);
	        File destFile = new File(dest);
	        
	        long fileSize = srcFile.length();
	        if(fileSize < size * 1024)   //文件大于size k时，才进行缩放,注意：size以K为单位
	            return;
	        
	        Double rate = (size * 1024 * 0.5) / fileSize; // 获取长宽缩放比例
	        
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
	                System.out.println("文件夹是空的!");
	                return;
	            } else {
	                for (File file2 : files) {
	                    if (file2.isDirectory()) {
	                        System.out.println("文件夹:" + file2.getAbsolutePath());
	                        traverseFolder2(file2.getAbsolutePath());
	                        try {
								zoomImage(file2.getAbsolutePath(), "C:\\Users\\lenovo\\Downloads", 32, 32);
								System.out.println("ok!");
							} catch (Exception e) {
								e.printStackTrace();
							}
	                    } else {
	                        System.out.println("文件:" + file2.getAbsolutePath());
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
	            System.out.println("文件不存在!");
	        }
	    }
	 
	 public static void main(String[] args) {
		 traverseFolder2("C:\\Users\\lenovo\\Downloads\\map-marker");
	}
	 
	 

	    /** 
	     * @param im 
	     *            原始图像 
	     * @param resizeTimes 
	     *            倍数,比如0.5就是缩小一半,0.98等等double类型 
	     * @return 返回处理后的图像 
	     */  
	    public BufferedImage zoomImage(String src) {  
	          
	        BufferedImage result = null;  
	  
	        try {  
	            File srcfile = new File(src);  
	            if (!srcfile.exists()) {  
	                System.out.println("文件不存在");  
	                  
	            }  
	            BufferedImage im = ImageIO.read(srcfile);  
	  
	            /* 原始图像的宽度和高度 */  
	            int width = im.getWidth();  
	            int height = im.getHeight();  
	              
	            //压缩计算  
	            float resizeTimes = 0.3f;  /*这个参数是要转化成的倍数,如果是1就是转化成1倍*/  
	              
	            /* 调整后的图片的宽度和高度 */  
	            int toWidth = (int) (width * resizeTimes);  
	            int toHeight = (int) (height * resizeTimes);  
	  
	            /* 新生成结果图片 */  
	            result = new BufferedImage(toWidth, toHeight,  
	                    BufferedImage.TYPE_INT_RGB);  
	  
	            result.getGraphics().drawImage(  
	                    im.getScaledInstance(toWidth, toHeight,  
	                            java.awt.Image.SCALE_SMOOTH), 0, 0, null);  
	              
	  
	        } catch (Exception e) {  
	            System.out.println("创建缩略图发生异常" + e.getMessage());  
	        }  
	          
	        return result;  
	  
	    }  
	      
	     public boolean writeHighQuality(BufferedImage im, String fileFullPath) {  
	            try {  
	                /*输出到文件流*/  
	                FileOutputStream newimage = new FileOutputStream(fileFullPath);  
	                //JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(newimage);  
	               // JPEGEncodeParam jep = JPEGCodec.getDefaultJPEGEncodeParam(im);  
	                /* 压缩质量 */  
	                //jep.setQuality(0.9f, true);  
	               // encoder.encode(im, jep);  
	               /*近JPEG编码*/  
	                newimage.close();  
	                return true;  
	            } catch (Exception e) {  
	                return false;  
	            }  
	        }  
	       
	       
}
