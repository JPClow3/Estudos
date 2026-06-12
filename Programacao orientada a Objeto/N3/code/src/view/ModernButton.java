package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ModernButton extends JButton {
    private Color defaultBackgroundColor;
    private Color hoverBackgroundColor;

    public ModernButton(String text, Color bg, Color hoverBg, Color fg) {
        super(text);
        this.defaultBackgroundColor = bg;
        this.hoverBackgroundColor = hoverBg;
        
        setFont(StyleGuide.FONTE_TEXTO);
        setBackground(defaultBackgroundColor);
        setForeground(fg);
        setFocusPainted(false);
        setContentAreaFilled(false); // We will paint the background ourselves
        setOpaque(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Remove traditional border to paint our own round rectangle or flat design
        setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(hoverBackgroundColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(defaultBackgroundColor);
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        if (getModel().isPressed()) {
            g2.setColor(getBackground().darker());
        } else {
            g2.setColor(getBackground());
        }
        
        // Draw rounded rectangle
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
        g2.dispose();
        
        super.paintComponent(g);
    }
}
