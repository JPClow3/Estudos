import java.sql.Time;
import java.util.Date;

public class Movimento {

    public static final int DEPOSITO = 1;
    public static final int SAQUE = 2;

    private int tipo_mov;
    private Date dt_mov;
    private Time hor_mov;
    private double val_mov;

    public Movimento() {}

    public Movimento(int tipo_mov, Date dt_mov, Time hor_mov, double val_mov) {
        this.tipo_mov = tipo_mov;
        this.dt_mov = dt_mov;
        this.hor_mov = hor_mov;
        this.val_mov = val_mov;
    }

    public int getTipo_mov() { return tipo_mov; }
    public void setTipo_mov(int tipo_mov) { this.tipo_mov = tipo_mov; }

    public Date getDt_mov() { return dt_mov; }
    public void setDt_mov(Date dt_mov) { this.dt_mov = dt_mov; }

    public Time getHor_mov() { return hor_mov; }
    public void setHor_mov(Time hor_mov) { this.hor_mov = hor_mov; }

    public double getVal_mov() { return val_mov; }
    public void setVal_mov(double val_mov) { this.val_mov = val_mov; }
}