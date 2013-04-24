
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
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
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;
import org.joda.time.DateTime;

//@author daniel 

public class MainFrame extends JPanel{
  
       private JLabel eFechaInicial,eFechaFinal; 
       private JTextField cFechaInicial,cFechaFinal; 
       private JButton bGenerarReporte,bVistaPrevia;
       private JTable tPrevisualizacion = new JTable(new DefaultTableModel());              
       private JScrollPane spt;       
       private JPanel pDatos,pPrevisualizacion;
       private DefaultTableModel modelo = (DefaultTableModel)tPrevisualizacion.getModel();              
       private static Map<Integer,ArrayList<Object>> mapaFechas;
       private static Map<Integer,String> mapaNombres; 
       private static Map<String,ArrayList<Object>> mapaCombinado;
       
       public MainFrame(){
             
              System.out.println("En el constructor");
              
              setSize(600,500);
              setLayout(new GridBagLayout());                            
                            
              GridBagConstraints gbc = new GridBagConstraints();                            
              
              tPrevisualizacion.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
              
              eFechaInicial = new JLabel("Fecha inicial: ");
              eFechaFinal = new JLabel("Fecha final: ");
              
              cFechaInicial = new JTextField(6);
              cFechaFinal = new JTextField(6);
              
              bVistaPrevia = new JButton("Vista Previa");
              bVistaPrevia.addActionListener(new ActionListener() {

                           @Override
                           public void actionPerformed(ActionEvent e) {
                               
                                  SwingWorker worker;
                                  worker = new SwingWorker() {

                                  @Override
                                  protected Void doInBackground() throws Exception {
                                          
                                            MainFrame.this.setDatosBase();
                                                                    
                                            Set keysFechas  = mapaFechas.keySet();
                                  
                                            String cfi = MainFrame.this.cFechaInicial.getText().trim();
                                            String cff = MainFrame.this.cFechaFinal.getText().trim();
                                  
                                            int diafi = Integer.parseInt(cfi.substring(0,2));
                                            int mesfi = Integer.parseInt(cfi.substring(3,5));
                                            int añofi = Integer.parseInt(cfi.substring(6,10));
                                  
                                            int diaff = Integer.parseInt(cff.substring(0,2));
                                            int mesff = Integer.parseInt(cff.substring(3,5));
                                            int añoff = Integer.parseInt(cff.substring(6,10));
                                    
                                            System.out.println(diafi + " - " + mesfi + " - " + añofi);
                                            DateTime fechaInicial = new DateTime(añofi,mesfi,diafi,0,0);
                                            DateTime fechaFinal = new DateTime(añoff,mesff,diaff,0,0);
                                  
                                            SimpleDateFormat sdfFecha = new SimpleDateFormat("dd/MM/yyyy", new Locale("es","MX"));
                                            SimpleDateFormat sdfHoras = new SimpleDateFormat("HH:mm:ss", new Locale("es","MX"));
                                  
                                            Object[] renglon = new Object[6];                                  
                                            int k = 0 ,h = 0, j = 0,l = 0;                                  
                                            int diaTemp = -1;   
                                  
                                            boolean[] existeDato = {false,false,false,false};
                                  
                                            for( Object id : keysFechas ){
                                                                                 
                                                 ArrayList<Object> fechas = mapaFechas.get((Integer)id);
                                                                                                                     
                                                 for( Object fecha : fechas ){
                                                                                                                                                                   
                                                      Date date = (Date)fecha;
                                                      DateTime Fecha = new DateTime(date);                                                
                                                
                                                      if( Fecha.toDate().after(fechaInicial.toDate()) && Fecha.toDate().before(fechaFinal.plusDays(1).toDate()) ){                                                                                                        
                                                    
                                                          String nombre = mapaNombres.get((Integer)id);
                                                          String fff = sdfFecha.format(date);
                                                          String ff = sdfHoras.format(date);
                                                                                                                                                        
                                                          int diaF = Fecha.getDayOfMonth();                                                                                                                                                            
                                                          int horF = Fecha.getHourOfDay();                                                                                                        
                                                          int minF = Fecha.getMinuteOfHour();                                                                                                                                                                                                                                                                      
                                                    
                                                          System.out.println(nombre + " - " + horF + " " + minF + " " + fecha);   
                                                          
                                                          renglon[0]= nombre;                                                                                                
                                                          renglon[1] = fff;
                                                          
                                                          if( diaTemp != diaF ){
                                                              
                                                              if( k < 1 ){
                                                                  renglon[2] = null;           
                                                                  k = 0;
                                                              }
                                                              if( h < 1 ){
                                                                  renglon[3] = null;           
                                                                  h = 0;
                                                              }
                                                              if( j < 1 ){
                                                                  renglon[4] = null;           
                                                                  j = 0;
                                                              }
                                                              if( l < 1 ){
                                                                  renglon[5] = null;           
                                                                  l = 0;
                                                              }
                                                    
                                                              MainFrame.this.modelo.addRow(renglon);
                                                              renglon = new Object[6];                                                                                                                                             
                                                              diaTemp = diaF;                                                                                                           
                                                     
                                                          }                                                                                                                                                                                                                                                                                                                                                                           
                                                    
                                                          if( horF >= 7 && horF <= 9 ){
                                                              renglon[2] = ff;       
                                                              k++;
                                                              existeDato[0] = true;
                                                          }                                                                                                     
                                                    
                                                          if( horF >= 12 && horF <= 14 ){
                                                              renglon[3] = ff;       
                                                              h++;
                                                              existeDato[1] = true; 
                                                          }                                                                                                        
                                                    
                                                          if( horF >= 15 && horF <= 17 ){
                                                              renglon[4] = ff;       
                                                              j++;
                                                              existeDato[2] = true;
                                                          }
                                                                                                                                                                
                                                          if( horF >= 17 && minF >= 30 ){
                                                              renglon[5] = ff;       
                                                              l++;
                                                              existeDato[3] = true;       
                                                          }
                                                                                                                                                                                                                                                                                                                                                 
                                                      }                                                                                          
                                       
                                                 }                                                                                                                                                                                                                                                                                                                                         
                                                                    
                                            }
                                  
                                            return null;
                                            
                                  }
                                  
                          };
                      
                          worker.execute();
                                  
                    }
              
                 }
                 
              );
              
              bGenerarReporte = new JButton("Generar Reporte");
              bGenerarReporte.addActionListener(new ActionListener() {

                              @Override
                              public void actionPerformed(ActionEvent e) {
                                     
                              }
              });
              
              modelo.addColumn("Nombre Trabajador");
              modelo.addColumn("Fecha");
              modelo.addColumn("Hora Entrada");
              modelo.addColumn("Hora Salida");
              modelo.addColumn("Hora Entrada");
              modelo.addColumn("Hora Salida"); 
              
              JPanel pBotones = new JPanel();
              
              pBotones.add(eFechaInicial);
              pBotones.add(cFechaInicial);
              pBotones.add(eFechaFinal);
              pBotones.add(cFechaFinal);
              pBotones.add(bVistaPrevia);
              pBotones.add(bGenerarReporte);              
              
              gbc.gridwidth = 4;              
              add(pBotones,gbc);
              
              pPrevisualizacion = new JPanel();              
              pPrevisualizacion.setSize(500,300);
              spt = new JScrollPane(tPrevisualizacion);              
              pPrevisualizacion.add(spt);                                                       
              
              gbc = new GridBagConstraints();
              gbc.gridheight = 5;
              gbc.gridwidth = 4;
              gbc.gridx = 0;
              gbc.gridy = 1;
              gbc.weighty = 0.1;
              
              add(pPrevisualizacion,gbc);
              
       }
       
       public void setDatosBase(){ 
                           
              System.out.println("En setDatosBase");
              
              Connection c;
              Statement s;
              ResultSet rs;
              mapaFechas = new HashMap<>();
              
              try{ 
                   
                  Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
                    
                  String base = "jdbc:odbc:Try";
                   
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
                         mapaNombres.put(id, nombre);
                         ids.add(new Integer(id));
                         j++;
                  }                   
                                      
                   
                  String fInicial = cFechaInicial.getText().trim();
                  String fFinal = cFechaFinal.getText().trim();
                  
                  System.out.println(fInicial + " " + fFinal);
                  
                  int i = 1;
                  for(Integer id : ids){
                       
                      select = "select checktime,userid from checkinout where userid = " + id + "";                      
                      rs = s.executeQuery(select);                       
                      ArrayList<Object> fechas = new ArrayList<>();
                      
                      while( rs.next() ){
                       
                             Object dato = rs.getObject(1);                                                                                       
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
                               frame.add(new MainFrame());
                               frame.setSize(650,550);
                               frame.setResizable(false);
                               frame.pack();
                               frame.setVisible(true);
                
                        }
                  });
                  
              }catch(Exception e){}
            
       }                
    
}
