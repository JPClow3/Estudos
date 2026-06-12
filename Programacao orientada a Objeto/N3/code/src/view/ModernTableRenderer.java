package view;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class ModernTableRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
        
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        // Remove the default cell focus border which looks outdated
        if (c instanceof JComponent) {
            ((JComponent) c).setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
            ((JComponent) c).setOpaque(true);
        }

        if (isSelected) {
            c.setBackground(StyleGuide.COR_PRIMARIA.brighter());
            c.setForeground(StyleGuide.BRANCO);
        } else {
            // Alternate row colors (Zebra pattern)
            if (row % 2 == 0) {
                c.setBackground(StyleGuide.BRANCO);
            } else {
                c.setBackground(StyleGuide.ZEBRA_ROW);
            }
            
            // Custom colors for status text
            if (value instanceof String) {
                String strValue = (String) value;
                if ("DEVOLVIDO".equals(strValue)) {
                    c.setForeground(StyleGuide.COR_SECUNDARIA);
                } else if ("ATRASADO".equals(strValue)) {
                    c.setForeground(StyleGuide.ERRO_ATRASO);
                } else if ("EMPRESTADO".equals(strValue)) {
                    c.setForeground(StyleGuide.COR_PRIMARIA);
                } else {
                    c.setForeground(StyleGuide.TEXTO_PRINCIPAL);
                }
            } else {
                c.setForeground(StyleGuide.TEXTO_PRINCIPAL);
            }
        }

        return c;
    }
}
