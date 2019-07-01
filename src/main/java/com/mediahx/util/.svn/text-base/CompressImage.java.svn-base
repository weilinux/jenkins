package com.mediahx.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.springframework.stereotype.Component;

import com.mediahx.util.gif.AnimatedGifEncoder;
import com.mediahx.util.gif.GifDecoder;
import com.mediahx.util.gif.Scalr;
import com.mediahx.util.gif.Scalr.Method;

import net.coobird.thumbnailator.Thumbnails;


/**
 * 压缩图片
 * 
 * @author ZHE
 *
 */
@Component
public class CompressImage {
	static Properties pro = PropertiesUtil.getProperties("properties/config.properties");
	private static final double COMPRESS_IMAGE_QUALITY;

	static {
		COMPRESS_IMAGE_QUALITY = Double.parseDouble(pro.getProperty("compress.image.quality"));
	}

	public static void processImage(String inputPath, String outputPath) {
		try {
			Thumbnails.of(inputPath).scale(1f).outputQuality(COMPRESS_IMAGE_QUALITY).toFile(outputPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void processGifImage(String inputPath, String outputPath) throws Exception {
		GifDecoder gd = new GifDecoder();
		int status = gd.read(new FileInputStream(new File(inputPath)));
		if (status != GifDecoder.STATUS_OK) {
			return;
		}
		//

		AnimatedGifEncoder ge = new AnimatedGifEncoder();
		ge.start(new FileOutputStream(new File(outputPath)));
		ge.setRepeat(0);
		
		int fc = gd.getFrameCount();
		int len=1;
		if(fc>=2){
			fc=1;
			len=2;
		}else{
			fc=0;
			len=1;
		}
		
		for (int i = fc; i < len; i++) {
			BufferedImage frame = gd.getFrame(i);
			int width = frame.getWidth();
			int height = frame.getHeight();
			// 80%
			width = (int) (width * 1);
			height = (int) (height * 1);
			//
//			BufferedImage rescaled = Scalr.resize(frame, Mode.FIT_EXACT, width, height);
			BufferedImage rescaled = Scalr.resize(frame, Method.SPEED, width, height);
//			BufferedImage rescaled = Thumbnails.of(frame).scale(1f).outputQuality(0.1).asBufferedImage();
			//
			int delay = gd.getDelay(i);
			//

			ge.setDelay(delay);
			ge.addFrame(rescaled);
		}

		ge.finish();
		//
	}
	

	public static void main(String[] args) throws Exception {
//		CompressImage.processImage("D:/temp/1523260224281_300504.gif", "D:/temp/aaa.gif");
		CompressImage.processGifImage("D:/temp/adacdbccde63c3ad800f46b2dad7ce90_1523261175921_297611.gif", "D:/temp/aaa.gif");
	}
}
