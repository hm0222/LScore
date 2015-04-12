package sjy.elwg.notation;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;

import javax.swing.JComponent;

import sjy.elwg.notation.musicBeans.Page;
import sjy.elwg.notation.musicBeans.Score;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * ����ͼƬ����
 * @author sjy
 *
 */
@SuppressWarnings("restriction")
public class ImageHandler {
	
	/**
	 * ��һ��JPanel����Ϊ��fileָ����ͼƬ��
	 * @param file
	 * @param panel
	 */
	@SuppressWarnings("restriction")
	public static void saveToPicture(File file, JComponent panel){
		BufferedImage image = new BufferedImage(Page.PAGE_WIDTH, Page.PAGE_HEIGHT, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = image.createGraphics();
		panel.paint(g);
		image.flush();  
		try {
			FileOutputStream fos = new FileOutputStream(file);
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(fos);
			JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(image);
			param.setQuality(1.0f, true);
			encoder.encode(image, param);
			javax.imageio.ImageIO.write(image, "jpeg", fos);
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	/**
	 * �����ױ���Ϊһϵ��ͼƬ������ÿҳΪһ��ͼƬ
	 * @param file �����Ŀ���ַ.���file���ļ������׸�ͼƬ������file�ļ������ͼƬ������ͬĿ¼�£�
	 *             ���Ը��ļ������ϰ�����������������file1, file2��. 
	 *             ���file���ļ��У���ͼƬ�����ڸ�Ŀ¼��.
	 * @param score ����
	 */
	public static void saveScoreToPictures(File file, Score score){
		if(score.getPageList().size() == 0)
			return;
		if(!file.isDirectory()){
			String append = file.getName().split("\\.")[1];
			saveToPicture(file, score.getPageList().get(0));
			
			String path = file.getAbsolutePath();
			int lindex = path.lastIndexOf(".");
			String pre = path.substring(0, lindex);
			
			for(int i = 1, n = score.getPageList().size(); i < n; i++){
				Page page = score.getPageList().get(i);
				File newFile = new File(pre + String.valueOf(i+1) + "." + append);
				saveToPicture(newFile, page);
			}
		}
	}

}
