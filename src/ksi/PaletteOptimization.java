/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ksi;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Syndarin
 */
public class PaletteOptimization {

    /**
     *
     * @param source
     * @return
     */
    // фиксированная палитра
    public static Image SetFixedPalette(Image source) {
        Image output;
        int width = source.getWidth(null);
        int height = source.getHeight(null);

        // массивы исходных и преобразованных пикселов
        int source_pixels[] = new int[width * height];
        int output_pixels[] = new int[width * height];

        PixelGrabber grabber = new PixelGrabber(source, 0, 0, width, height, source_pixels, 0, width);
        try {
            // граббим исходные пикселы
            grabber.grabPixels();
            for (int i = 0; i < source_pixels.length; i++) {
                // битовыми операциями обрезаем все биты, кроме 1-го
                output_pixels[i] = (((source_pixels[i] >>> 31) << 31) | ((source_pixels[i] << 8) >>> 31) << 23) | (((source_pixels[i] << 16) >>> 31) << 15) | (((source_pixels[i] << 24) >>> 31) << 7);
            }
            // формируем выходное изображение
            MemoryImageSource ms = new MemoryImageSource(width, height, output_pixels, 0, width);
            output = Toolkit.getDefaultToolkit().createImage(ms);
            return output;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }


    }

    /**
     *
     * @param source
     * @return
     */
    public static Image SetOptimizedPalette(Image source) {
        // все по аналогии с предыдущим методом
        Image output;
        int width = source.getWidth(null);
        int height = source.getHeight(null);

        int source_pixels[] = new int[width * height];
        int output_pixels[] = new int[width * height];
        PixelGrabber grabber = new PixelGrabber(source, 0, 0, width, height, source_pixels, 0, width);
        try {
            grabber.grabPixels();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // формируем карту - код цвета и количество его повторений в картинке
        HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
        for (int i = 0; i < source_pixels.length; i++) {
            Map.Entry entry = null;
            // если цвет еще не занесен в карту, то заносим и присваиваем 1 
            if (!map.containsKey(source_pixels[i])) {
                map.put(source_pixels[i], 1);
            } else {
                // иначе увеличиваем количество повторов
                int c = map.get(source_pixels[i]);
                c++;
                map.put(source_pixels[i], c);
            }
        }

        // сортируем количество вхождений цвета по убыванию
        List entryList = new ArrayList(map.entrySet());
        Collections.sort(entryList, new Comparator() {

            @Override
            public int compare(Object o1, Object o2) {
                Map.Entry e1 = (Map.Entry) o1;
                Map.Entry e2 = (Map.Entry) o2;
                Comparable c1 = (Comparable) e1.getValue();
                Comparable c2 = (Comparable) e2.getValue();
                return c2.compareTo(c1);
            }
        });

        // определяем размер палитры изображения, т.к могут быть изображения с количеством цветов, меньшим 8
        int pal_size;
        if (entryList.size() > 8) {
            pal_size = 8;
        } else {
            pal_size = entryList.size();
        }
        
        // массив цветов, которые будут отобраны
        int[] colors = new int[pal_size];
        for (int i = 0; i < colors.length; i++) {
            // выбираем первые 8 цветов
            Map.Entry ens = (Map.Entry) entryList.get(i);
            colors[i] = (Integer) ens.getKey();
        }

        // считаем для каждого пиксела исходной картинки, какой из 8ми цветов обеспечивает меньшую погрешность
        // и присваиваем этот цвет соответствующему пикселу
        for (int i = 0; i < source_pixels.length; i++) {
            int minindex = 0;
            double d = 999999.99;
            for (int j = 0; j < colors.length; j++) {
                double err = Math.sqrt(Math.pow((double) (getR(colors[j]) - getR(source_pixels[i])), 2.0) + Math.pow((double) (getG(colors[j]) - getG(source_pixels[i])), 2.0) + Math.pow((double) (getB(colors[j]) - getB(source_pixels[i])), 2.0));
                if (err < d) {
                    d = err;
                    minindex = j;
                }
            }
            output_pixels[i] = colors[minindex];
        }

        // формируем выходную картинку
        MemoryImageSource ms = new MemoryImageSource(width, height, output_pixels, 0, width);
        output = Toolkit.getDefaultToolkit().createImage(ms);
        return output;
    }

    /**
     *
     * @param original
     * @param optimized
     * @return
     */
    public static Image MakeDithering(Image original, Image optimized) {
        Image output;
        int width = original.getWidth(null);
        int height = original.getHeight(null);
        int[] original_pixels = new int[width * height];// масс
        int[] optimized_pixels = new int[width * height];
        int[] output_pixels = new int[width * height];
        int[] pixels_correction = new int[width * height];

        PixelGrabber grabber_original = new PixelGrabber(original, 0, 0, width, height, original_pixels, 0, width);
        PixelGrabber grabber_optimized = new PixelGrabber(optimized, 0, 0, width, height, optimized_pixels, 0, width);

        try {
            grabber_original.grabPixels();
            grabber_optimized.grabPixels();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < original_pixels.length; i++) {
            pixels_correction[i] = original_pixels[i] ^ optimized_pixels[i];
            pixels_correction[i] = (((pixels_correction[i] << 8) >>> 26) << 16) | (((pixels_correction[i] << 16) >>> 26) << 8) | ((pixels_correction[i] << 24) >>> 26);
        }
        output_pixels = optimized_pixels.clone();
        for (int i = 0; i < height - 1; i++) {
            if (i % 2 == 0) {
                for (int j = 1; j < width - 1; j++) {
                    int index = (i * width) + j;

                    output_pixels[index + 1] = correctColor(output_pixels[index + 1], pixels_correction[index]);
                    output_pixels[index + width + 1] = correctColor(output_pixels[index + width + 1], pixels_correction[index]);
                    output_pixels[index + width] = correctColor(output_pixels[index + width], pixels_correction[index]);
                    output_pixels[index + width - 1] = correctColor(output_pixels[index + width - 1], pixels_correction[index]);
                }
            } else {
                for (int j = width - 1; j >= 1; j--) {
                    int index = (i * width) + j;
                    output_pixels[index - 1] = correctColor(output_pixels[index - 1], pixels_correction[index]);
                    output_pixels[index - width - 1] = correctColor(output_pixels[index - width - 1], pixels_correction[index]);
                    output_pixels[index - width] = correctColor(output_pixels[index - width], pixels_correction[index]);
                    output_pixels[index - width + 1] = correctColor(output_pixels[index - width + 1], pixels_correction[index]);
                }
            }
        }
        MemoryImageSource ms = new MemoryImageSource(width, height, output_pixels, 0, width);
        output = Toolkit.getDefaultToolkit().createImage(ms);
        return output;
    }

    private static int getR(int c) {
        return (c << 8) >>> 24;
    }

    private static int getG(int c) {
        return (c << 16) >>> 24;
    }

    private static int getB(int c) {
        return (c << 24) >>> 24;
    }

    private static int fromRGB(int r, int g, int b) {
        return (255 << 24) | (r << 16) | (g << 8) | b;
    }

    private static int correctColor(int source, int correction) {
        int r_correction = getR(correction);
        int g_correction = getG(correction);
        int b_correction = getB(correction);

        int r_out = getR(source) + r_correction;
        int g_out = getG(source) + g_correction;
        int b_out = getB(source) + b_correction;

        if (r_out > 255) {
            r_out = 255;
        }
        if (g_out > 255) {
            g_out = 255;
        }
        if (b_out > 255) {
            b_out = 255;
        }

        return fromRGB(r_out, g_out, b_out);
    }
}
