package Modelo;

public class Latte extends Bebida{
    private String tipoLeche;

    public Latte(String nombre, double precio, String tamaño, String tipoLeche) {
        super(nombre, precio, tamaño);
        this.tipoLeche = tipoLeche;
    }

    @Override

    public double calcularPrecio(){
        double precioFinal = getPrecioBase();

        if (this.tipoLeche.equalsIgnoreCase("Almendras")) { //El equalsIgnoreCase es para que sea lo mismo escribir almendras con mayus o sin mayus. 
            precioFinal += 400;
        }
        return precioFinal;
    }
    public String getTipoLeche() {
        return this.tipoLeche;
    }
}
