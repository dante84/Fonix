
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

//@author daniel 

public class MainFrame extends JPanel{
  
       private JLabel eFechaInicial,eFechaFinal; 
       private JTextField cFechaInicial,cFechaFinal; 
       private JButton bGenerarReporte,bVistaPrevia;
       private JTable tPrevisualizacion = new JTable(new DefaultTableModel());              
       private JScrollPane spt;       
       private DefaultTableModel modelo = (DefaultTableModel)tPrevisualizacion.getModel();              
       private static Map<Integer,ArrayList<Object>> mapaFechas;
       private static Map<Integer,String> mapaNombres; 
       private static Map<String,ArrayList<Object>> mapaCombinado;
       
       public MainFrame(){
             
              System.out.println("En el constructor");
              
              eFechaInicial = new JLabel("Fecha inicial: ");
              eFechaFinal = new JLabel("Fecha final: ");
              
              cFechaInicial = new JTextField(6);
              cFechaFinal = new JTextField(6);
              
              bVistaPrevia = new JButton("Vista Previa");
              bVistaPrevia.addActionListener(new ActionListener() {

                           @Override
                           public void actionPerformed(ActionEvent e) {
                                           
                                  MainFrame.this.setDatosBase();
                                  
                                  //Set keysNombres = mapaNombres.keySet();
                                  Set keysFechas  = mapaFechas.keySet();
                                  
                                  for( Object id : keysFechas ){
                                        
                                       //System.out.println(id + " -  " + mapaNombres.get(id));
                                       ArrayList<Object> fechas = mapaFechas.get(id);
                                       
                                       for( Object fecha : fechas ){
                                            System.out.println( id + " " + mapaNombres.get(id) + " " + fecha ); 
                                       }
                                      
                                  }
                                  
                                  
                                  
                           }
              });
              
              bGenerarReporte = new JButton("Generar Reporte");
              bGenerarReporte.addActionListener(new ActionListener() {

                              @Override
                              public void actionPerformed(ActionEvent e) {
                                     
                              }
              });
              
              add(eFechaInicial);
              add(cFechaInicial);
              add(eFechaFinal);
              add(cFechaFinal);
              add(bVistaPrevia);
              add(bGenerarReporte);
              
       }
       
       public void setDatosBase(){ 
                           
              System.out.println("En setDatosBase");
              
              Connection c;
              Statement s;
              ResultSet rs;
              mapaFechas = new HashMap<>();
              
              try{ 
                   
                  Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
                    
                  String base = "jdbc:odbc:Test";
                   
                  Properties props = new Properties();
                  props.put ("charSet", "iso-8859-1");
                  c = DriverManager.getConnection(base,props);
                  s = c.createStatement();                   
                   
                  String select = "select userid,Name from USERINFO where Name not like '#N/A'";
                   
                  rs = s.executeQuery(select);
                           
                  ArrayList<Integer> ids = new ArrayList<>();
                  mapaNombres = new HashMap<>();
                  
                  System.out.println("En el try antes del while");
                  
                  int j = 1;
                  while( rs.next() ){
                         String nombre = rs.getString(2);
                         int id = rs.getInt(1);
                         //System.out.println(j + "  -  " + nombre + " " + id);
                         mapaNombres.put(id, nombre);
                         ids.add(new Integer(id));
                         j++;
                  }                   
                                      
                   
                  String fInicial = cFechaInicial.getText().trim();
                  String fFinal = cFechaFinal.getText().trim();
                  
                  System.out.println(fInicial + " " + fFinal);
                  
                  int i = 1;
                  for(Integer id : ids){
                       
                      select = "select checktime,userid from checkinout where userid = '" + id + "' and checktime >= " + fInicial + " and checktime <= " + fFinal + "";
                      System.out.println(select);
                      rs = s.executeQuery(select);                       
                      ArrayList<Object> fechas = new ArrayList<>();
                      
                      while( rs.next() ){
                       
                             Object dato = rs.getObject(1);                                                          
                             //System.out.println(i + " -  " + dato);
                             fechas.add(dato);
                             i++;
                             
                      }
                      
                      mapaFechas.put(id,fechas);
                       
                      rs.close();                                              
                       
                  }
                                       
                  s.close();
                  c.close();
                   
              }catch(ClassNotFoundException | SQLException e){ e.printStackTrace(); }
                                                                             
                          
       }    
       
       public static void main(String args[]){
         
              try{
                  
                  javax.swing.SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                              
                               JFrame frame = new JFrame("Reporte");
                               frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                               frame.setResizable(false);
                               frame.add(new MainFrame());
                               frame.pack();
                               frame.setVisible(true);
                
                        }
                  });
                  
              }catch(Exception e){}
            
       }                
    
}
