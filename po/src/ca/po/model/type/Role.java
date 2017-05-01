package ca.po.model.type;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;



/**
 * @author MOUELLET
 * Enumerated type stored as a value object in the relationship table
 * po_user_role.  this relationship table is also used by the 
 * security realm of the servlet web container. 
 * 
 */
public class Role {
   
   public static final Role PO_ADMIN= new Role("PO_Admin");
   public static final Role PO_USER= new Role("PO_User");

   public static final Map ROLES = new HashMap();
   public static final Collection ROLES_LIST = ROLES.values();
   static {
       ROLES.put(PO_ADMIN.toString(),PO_ADMIN);
       ROLES.put(PO_USER.toString(),PO_USER);
   }

   
   public static Role GET_ROLE(String name){
       return (Role) ROLES.get(name);
   }
   
   private String name;
   private String nameLabel;
      
   private Role(String name){
       this.name = name;
       this.nameLabel = name.substring(3);
   }

   Object readResolve() {
       return GET_ROLE(name);
   }
   
   public String toString(){
       return this.name;
   }
   
   /*
    * todo: the idea here is to have a local representation of role
    * for now, just return the Role skipping the "PO_" part
    */
   public String getNameLabel(){
       return nameLabel;
   }
   
   public String getName(){
       return name;
   }
   
   
}
