package view;

import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.Component;

@SuppressWarnings("this-escape")
public class ModernTableHeaderRenderer extends DefaultTableCellRenderer {
    private static final long serialVersionUID = 1L;

    public ModernTableHeaderRenderer() {
        setHorizontalAlignment(SwingConstants.LEFT);
        setOpaque(true);
        setFont(StyleGuide.FONTE_TEXTO);
        setBackground(StyleGuide.COR_PRIMARIA);
        setForeground(StyleGuide.BRANCO);
        setBorder(BorderFactory.createEmptyBorder(7, 10, 7, 10));
    }

    @Override
    public Component getTableCellRendererComponent(
            JTable table,
            Object value,
            boolean isSelected,
            boolean hasFocus,
            int row,
            int column
    ) {
        Component component = super.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column
        );
        component.setBackground(StyleGuide.COR_PRIMARIA);
        component.setForeground(StyleGuide.BRANCO);
        component.setFont(StyleGuide.FONTE_TEXTO);
        return component;
    }
}
