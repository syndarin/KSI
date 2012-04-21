/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ksi;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Syndarin
 */
public class UserForm {

    private JFrame frame;
    private JLabel lbl_source;
    private JLabel lbl_optimized;
    private JLabel lbl_toned;
    private JFileChooser f_chooser;
    private static File f;

    /**
     * 
     */
    public UserForm() {
        // создаем форму
        frame = new JFrame("Компьютерный синтез изображений");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        lbl_source = new JLabel(new ImageIcon("bg.png"));
        lbl_optimized = new JLabel(new ImageIcon("bg.png"));
        lbl_toned = new JLabel(new ImageIcon("bg.png"));
        f_chooser = new JFileChooser();
        f_chooser.setFileFilter(new FileNameExtensionFilter("Файлы изображений", "bmp", "gif", "jpg", "png"));

        JMenuBar menu = new JMenuBar();
        JMenu m1 = new JMenu("Файл");
        JMenu m2 = new JMenu("Оптимизация палитры");

        JMenuItem mi1 = new JMenuItem("Открыть");
        JMenuItem mi2 = new JMenuItem("Выход");
        JMenuItem mi3 = new JMenuItem("Квантование");
        JMenuItem mi4 = new JMenuItem("Метод популярности");

        mi1.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                f_chooser.showOpenDialog(null);
                try {
                    // открываем файл и загружаем картинку
                    f = f_chooser.getSelectedFile();
                    lbl_source.setIcon(new ImageIcon(f.getAbsolutePath()));
                    lbl_optimized.setIcon(new ImageIcon("bg.png"));
                    lbl_toned.setIcon(new ImageIcon("bg.png"));
                } catch (Exception exp) {
                    JOptionPane.showMessageDialog(frame, "Выберите файл для преобразования!");
                }
            }
        });
        mi2.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // обработчик пункта "выход"
                System.exit(0);
            }
        });
        mi3.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (f == null) {
                    JOptionPane.showMessageDialog(frame, "Выберите файл для преобразования!");
                } else {
                    // считываем картинку в память
                    Image im_source = Toolkit.getDefaultToolkit().getImage(f.getAbsolutePath());
                    Image optimized = PaletteOptimization.SetFixedPalette(im_source);
                    lbl_optimized.setIcon(new ImageIcon(optimized));
                    Image dithered = PaletteOptimization.MakeDithering(im_source, optimized);
                    lbl_toned.setIcon(new ImageIcon(dithered));
                }
            }
        });
        mi4.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (f == null) {
                    JOptionPane.showMessageDialog(frame, "Выберите файл для преобразования!");
                } else {
                    Image im_source = Toolkit.getDefaultToolkit().getImage(f.getAbsolutePath());
                    Image optimized = PaletteOptimization.SetOptimizedPalette(im_source);
                    lbl_optimized.setIcon(new ImageIcon(optimized));
                    Image dithered = PaletteOptimization.MakeDithering(im_source, optimized);
                    lbl_toned.setIcon(new ImageIcon(dithered));
                }
            }
        });

        m1.add(mi1);
        m1.addSeparator();
        m1.add(mi2);
        m2.add(mi3);
        m2.add(mi4);
        menu.add(m1);
        menu.add(m2);

        frame.setJMenuBar(menu);
        frame.setSize(new Dimension(800, 400));

        frame.setLayout(new FlowLayout());

        frame.add(lbl_source);
        frame.add(lbl_optimized);
        frame.add(lbl_toned);
        frame.pack();

        frame.setVisible(true);
    }
}
