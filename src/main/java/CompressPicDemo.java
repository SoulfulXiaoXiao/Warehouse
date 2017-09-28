/*
 *  ����ͼʵ�֣���ͼƬ(jpg��bmp��png��gif�ȵ�)��ʵ�ı����Ҫ�Ĵ�С 
 */


import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import java.awt.Rectangle;

/*******************************************************************************
 * ����ͼ�ࣨͨ�ã� ��java���ܽ�jpg��bmp��png��gifͼƬ�ļ������еȱȻ�ǵȱȵĴ�Сת���� ����ʹ�÷���
 * compressPic(��ͼƬ·��,����СͼƬ·��,��ͼƬ�ļ���,����СͼƬ����,����СͼƬ���,����СͼƬ�߶�,�Ƿ�ȱ�����(Ĭ��Ϊtrue))
 * ͼƬ��ȡ  readUsingImageReader
 */
public class CompressPicDemo {
	private File file = null; // �ļ�����
	private String inputDir; // ����ͼ·��
	private String outputDir; // ���ͼ·��
	private String inputFileName; // ����ͼ�ļ���
	private String outputFileName; // ���ͼ�ļ���
	private int outputWidth = 120; // Ĭ�����ͼƬ��
	private int outputHeight = 120; // Ĭ�����ͼƬ��
	private boolean proportion = true; // �Ƿ�ȱ����ű��(Ĭ��Ϊ�ȱ�����)

	public CompressPicDemo() { // ��ʼ������
		inputDir = "";
		outputDir = "";
		inputFileName = "";
		outputFileName = "";
		outputWidth = 120;
		outputHeight = 120;
	}

	public void setInputDir(String inputDir) {
		this.inputDir = inputDir;
	}

	public void setOutputDir(String outputDir) {
		this.outputDir = outputDir;
	}

	public void setInputFileName(String inputFileName) {
		this.inputFileName = inputFileName;
	}

	public void setOutputFileName(String outputFileName) {
		this.outputFileName = outputFileName;
	}

	public void setOutputWidth(int outputWidth) {
		this.outputWidth = outputWidth;
	}

	public void setOutputHeight(int outputHeight) {
		this.outputHeight = outputHeight;
	}

	public void setWidthAndHeight(int width, int height) {
		this.outputWidth = width;
		this.outputHeight = height;
	}

	/*
	 * ���ͼƬ��С ������� String path ��ͼƬ·��
	 */
	public long getPicSize(String path) {
		file = new File(path);
		return file.length();
	}

	// ͼƬ����
	public String compressPic() {
		try {
			// ���Դ�ļ�
			file = new File(inputDir + inputFileName);
			if (!file.exists()) {
				return "";
			}
			Image img = ImageIO.read(file);
			// �ж�ͼƬ��ʽ�Ƿ���ȷ
			if (img.getWidth(null) == -1) {
				System.out.println(" can't read,retry!" + "<BR>");
				return "no";
			} else {
				int newWidth;
				int newHeight;
				// �ж��Ƿ��ǵȱ�����
				if (this.proportion == true) {
					// Ϊ�ȱ����ż��������ͼƬ��ȼ��߶�
					double rate1 = ((double) img.getWidth(null))
							/ (double) outputWidth + 0.1;
					double rate2 = ((double) img.getHeight(null))
							/ (double) outputHeight + 0.1;
					// �������ű��ʴ�Ľ������ſ���
					double rate = rate1 > rate2 ? rate1 : rate2;
					newWidth = (int) (((double) img.getWidth(null)) / rate);
					newHeight = (int) (((double) img.getHeight(null)) / rate);
				} else {
					newWidth = outputWidth; // �����ͼƬ���
					newHeight = outputHeight; // �����ͼƬ�߶�
				}
				BufferedImage tag = new BufferedImage((int) newWidth,
						(int) newHeight, BufferedImage.TYPE_INT_RGB);

				/*
				 * Image.SCALE_SMOOTH �������㷨 ��������ͼƬ��ƽ���ȵ� ���ȼ����ٶȸ� ���ɵ�ͼƬ�����ȽϺ� ���ٶ���
				 */
				tag.getGraphics().drawImage(
						img.getScaledInstance(newWidth, newHeight,
								Image.SCALE_SMOOTH), 0, 0, null);
				FileOutputStream out = new FileOutputStream(outputDir
						+ outputFileName);
				// JPEGImageEncoder������������ͼƬ���͵�ת��
				JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
				encoder.encode(tag);
				out.close();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return "ok";
	}

	public String compressPic(String inputDir, String outputDir,
			String inputFileName, String outputFileName) {
		// ����ͼ·��
		this.inputDir = inputDir;
		// ���ͼ·��
		this.outputDir = outputDir;
		// ����ͼ�ļ���
		this.inputFileName = inputFileName;
		// ���ͼ�ļ���
		this.outputFileName = outputFileName;
		return compressPic();
	}

	public String compressPic(String inputDir, String outputDir,
			String inputFileName, String outputFileName, int width, int height,
			boolean gp) {
		// ����ͼ·��
		this.inputDir = inputDir;
		// ���ͼ·��
		this.outputDir = outputDir;
		// ����ͼ�ļ���
		this.inputFileName = inputFileName;
		// ���ͼ�ļ���
		this.outputFileName = outputFileName;
		// ����ͼƬ����
		setWidthAndHeight(width, height);
		// �Ƿ��ǵȱ����� ���
		this.proportion = gp;
		return compressPic();
	}

	/**
	 * ��ȡͼƬ
	 * @param src
	 * @param dest
	 * @param w
	 * @param h
	 * @param format
	 * @throws Exception
	 */
	public void readUsingImageReader(String src, String dest, int w, int h,String format)
			   throws Exception {
			  // ȡ��ͼƬ������
			  Iterator readers = ImageIO.getImageReadersByFormatName(format);
			  ImageReader reader = (ImageReader) readers.next();
			  // ȡ��ͼƬ������
			  InputStream source = new FileInputStream(src);
			  ImageInputStream iis = ImageIO.createImageInputStream(source);
			  reader.setInput(iis, true);
			  // ͼƬ����
			  ImageReadParam param = reader.getDefaultReadParam();
			  //Ĭ�Ͻ�ȡ��0,0 ��ʼ�������Լ���������
			  Rectangle rect = new Rectangle(0, 0, w, h);
			  param.setSourceRegion(rect);
			  BufferedImage bi = reader.read(0, param);
			  ImageIO.write(bi, format, new File(dest));
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
								System.out.println("ok!");
							} catch (Exception e) {
								e.printStackTrace();
							}
	                    } else {
	                        System.out.println("�ļ�:" + file2.getAbsolutePath());
	                        try {
	                        	CompressPicDemo t = new CompressPicDemo();
	                        	t.readUsingImageReader(file2.getAbsolutePath(), "d://"+file2.getName(), 60, 60,"png");
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
	// main����
	// compressPic(��ͼƬ·��,����СͼƬ·��,��ͼƬ�ļ���,����СͼƬ����,����СͼƬ���,����СͼƬ�߶�,�Ƿ�ȱ�����(Ĭ��Ϊtrue))
	public static void main(String[] arg) {
//		CompressPicDemo mypic = new CompressPicDemo();
////		System.out.println("�����ͼƬ��С��" + mypic.getPicSize("e:\\1.jpg") / 1024
////				+ "KB");
//		int count = 0; // ��¼ȫ��ͼƬѹ������ʱ��
//		File f = new File("D:\\PIC\\1\\");
//		File[] files = f.listFiles();
//		int i = 1;
//		for (File file : files) {
//			int start = (int) System.currentTimeMillis(); // ��ʼʱ��
//			mypic.compressPic("D:\\PIC\\1\\", "D:\\PIC\\2\\", file.getName(),
//					file.getName(), mypic.outputHeight, mypic.outputWidth, true);
//			int end = (int) System.currentTimeMillis(); // ����ʱ��
//			int re = end - start; // ��ͼƬ���ɴ���ʱ��
//			count += re;
//			System.out.println("��" + (i + 1) + "��ͼƬѹ������ʹ����: " + re + "����");
//			i++;
//		}
//		System.out.println("�ܹ����ˣ�" + count + "����");
//		CompressPicDemo t = new CompressPicDemo();
//		  try {
//			t.readUsingImageReader("d://test01.png", "d://3.png", 60, 60,"png");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		traverseFolder2("C:\\Users\\lenovo\\Downloads\\map-marker");
	}
}
