
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

/**
 *
 * @author dell
 */
public class Homepage extends javax.swing.JFrame {

    /**
     * Creates new form Homepage
     */
    JFileChooser openfile;
    File fichier;
    
    enum Tokentype{
        IDENT,MRINT,FINIST,FINLIGNE,VIR,MRREEL,MRST,MRBOOL,MRADD,MRSUB,MRMUL,MRDIV,MRPUT,MRFROM,MRSET,MRGET,ENTIER,REAL,IF,ELSE,CRG,CRD, 
        EQEQ,LTE,GTE,LT,GT,AND,OR,NEQ,TEXT,MRBEGIN,MREND,MRWHILE,ENDWHILE,COM,STRING,DQ,ENDST,FALSE,TRUE,BOOL,ENDIF,BADTOK,NO;
    }
    
    class Token{
        Tokentype type;
        int pos;
        int ligne;
        int declarer=0;
        Tokentype ty;
        int asign=0;
        String tx;
        float valeur;
        String te;
        String st;
        public Token(Tokentype type, int pos, int ligne, String tx, float val){
            this.type=type;
            this.pos=pos;
            this.ligne=ligne;
            this.tx=tx;
            this.valeur=val;
        }

    }
    
    public class Lexer {
    FileReader fr;
    BufferedReader bf;
    int index;
    int start;
    int pos;
    int ligncpt=0;
    String lign;
    String sub;
    int val;
    float valf;
    ArrayList<Token> arrtok=new ArrayList<Token>();
    
    ArrayList<Token> tokenizer(File f) throws FileNotFoundException, IOException{
        fr=new FileReader(f);
        bf=new BufferedReader(fr);
        while((lign=bf.readLine())!=null){
            lign=lign.concat("\n");
            System.out.println(lign.charAt(lign.length()-1));
            ligncpt++;
            index=0;
            while(index<=lign.length()-1){
                if(lign.charAt(index)=='\n'){
                    arrtok.add(new Token(Tokentype.FINLIGNE,index,ligncpt,"\n",0));
                    index++;
                }
                else if(lign.charAt(index)=='&'){
                    start=index+1;
                    index++;
                    if(lign.charAt(index)=='&'){
                        arrtok.add(new Token(Tokentype.AND,start,ligncpt,"&&",0));
                        index++;
                    }
                    else{
                        arrtok.add(new Token(Tokentype.BADTOK,start,ligncpt,"&",0));
                    }
                }
                else if(lign.charAt(index)=='|'){
                    start=index+1;
                    index++;
                    if(lign.charAt(index)=='|'){
                        arrtok.add(new Token(Tokentype.OR,start,ligncpt,"||",0));
                        index++;
                    }
                    else{
                        arrtok.add(new Token(Tokentype.BADTOK,start,ligncpt,"|",0));
                    }
                }
               else if(lign.charAt(index)==' '||lign.charAt(index)=='\t'){
                    while((lign.charAt(index)==' '||lign.charAt(index)=='\t')&&index<lign.length()-1)
                        index++;
                }
               else if(lign.charAt(index)=='%'){
                    start=index;
                    index++;
                    if(lign.charAt(index)!='.'){
                        sub=lign.substring(start, index);
                        arrtok.add(new Token(Tokentype.COM,start,ligncpt,sub,0));
                        if(lign.charAt(index)!='%'){
                            while(lign.charAt(index)!='%'&&index<lign.length()-1)
                                index++;
                            sub=lign.substring(start+1, index);
                            arrtok.add(new Token(Tokentype.TEXT,start+1,ligncpt,sub,0));
                            if(lign.charAt(index)=='%'){
                                sub=lign.substring(index, index+1);
                                arrtok.add(new Token(Tokentype.COM,index,ligncpt,sub,0));
                                index++;
                            }
                        }
                        else{
                            index=index;
                        }
                    }
                    else{
                        sub=lign.substring(start, index+1);
                        arrtok.add(new Token(Tokentype.FINIST,start,ligncpt,sub,0));
                        index++;
                    }
                }
               else if(lign.charAt(index)==','){
                    start=index;
                    sub=lign.substring(start, index+1);
                    arrtok.add(new Token(Tokentype.VIR,start,ligncpt,sub,0));
                    index++;
                }
               else  if(lign.charAt(index)=='['){
                    start=index;
                    sub=lign.substring(start, index+1);
                    arrtok.add(new Token(Tokentype.CRG,start,ligncpt,sub,0));
                    index++;
                }
              else  if(lign.charAt(index)==']'){
                    start=index;
                    sub=lign.substring(start, index+1);
                    arrtok.add(new Token(Tokentype.CRD,start,ligncpt,sub,0));
                    index++;
                }
              else  if(lign.charAt(index)=='"'){
                    start=index;
                    sub=lign.substring(start, index+1);
                    arrtok.add(new Token(Tokentype.DQ,start,ligncpt,sub,0));
                    if(lign.charAt(index+1)!='"'){
                    index++;
                    while(lign.charAt(index)!='"'&&index<lign.length()-1)
                        index++;
                    sub=lign.substring(start+1, index);
                    arrtok.add(new Token(Tokentype.TEXT,start+1,ligncpt,sub,0));
                    if(lign.charAt(index)=='"'){
                        sub=lign.substring(index, index+1);
                        arrtok.add(new Token(Tokentype.DQ,index,ligncpt,sub,0));
                        index++;
                    }
                    }
                    else{
                        index++;
                    }
                }
              else  if(Character.isDigit(lign.charAt(index))){
                    start=index;
                    while(Character.isDigit(lign.charAt(index))&&index<lign.length()-1)
                        index++;
                    if(lign.charAt(index)!='.'){
                        sub=lign.substring(start, index);
                        val=Integer.parseInt(sub);
                        arrtok.add(new Token(Tokentype.ENTIER,start,ligncpt,sub,val));
                    }
                    else{
                        index++;
                        if(!Character.isDigit(lign.charAt(index))){
                            sub=lign.substring(start, index);
                            arrtok.add(new Token(Tokentype.BADTOK,start,ligncpt,sub,0));
                        }
                        else{
                            while(Character.isDigit(lign.charAt(index))&&index<lign.length()-1)
                                index++;
                            sub=lign.substring(start, index);
                            valf=Float.parseFloat(sub);
                            arrtok.add(new Token(Tokentype.REAL,start,ligncpt,sub,valf));
                        }
                        
                    }
                }
              else  if(lign.charAt(index)=='='){
                    start=index;
                    index++;
                    if(lign.charAt(index)=='='){
                        sub=lign.substring(start, index+1);
                        arrtok.add(new Token(Tokentype.EQEQ,start,ligncpt,sub,0));
                        index++;
                    }
                    else{
                        sub=lign.substring(start, index);
                        arrtok.add(new Token(Tokentype.BADTOK,start,ligncpt,sub,0));
                    }
                }
              else  if(lign.charAt(index)=='!'){
                    start=index;
                    index++;
                    if(lign.charAt(index)=='='){
                        sub=lign.substring(start, index+1);
                        arrtok.add(new Token(Tokentype.NEQ,start,ligncpt,sub,0));
                        index++;
                    }
                    else{
                        sub=lign.substring(start, index);
                        arrtok.add(new Token(Tokentype.BADTOK,start,ligncpt,sub,0));
                    }
                }
              else  if(lign.charAt(index)=='<'){
                    start=index;
                    index++;
                    if(lign.charAt(index)=='='){
                        sub=lign.substring(start, index+1);
                        arrtok.add(new Token(Tokentype.LTE,start,ligncpt,sub,0));
                        index++;
                    }
                    else{
                        sub=lign.substring(start, index);
                        arrtok.add(new Token(Tokentype.LT,start,ligncpt,sub,0));
                    }
                }
              else  if(lign.charAt(index)=='>'){
                    start=index;
                    index++;
                    if(lign.charAt(index)=='='){
                        sub=lign.substring(start, index+1);
                        arrtok.add(new Token(Tokentype.GTE,start,ligncpt,sub,0));
                        index++;
                    }
                    else{
                        sub=lign.substring(start, index);
                        arrtok.add(new Token(Tokentype.GT,start,ligncpt,sub,0));
                    }
                }
              else  if(Character.isLetter(lign.charAt(index))){
                    start=index;
                    while(Character.isLetterOrDigit(lign.charAt(index))&&index<lign.length()-1)
                        index++;
                    if(lign.charAt(index)=='_'){
                        index++;
                        if(!Character.isLetterOrDigit(lign.charAt(index))){
                            sub=lign.substring(start, index);
                            arrtok.add(new Token(Tokentype.BADTOK,start,ligncpt,sub,0));
                        }
                        else{
                        while(Character.isLetterOrDigit(lign.charAt(index))&&index<lign.length()-1)
                            index++;
                        sub=lign.substring(start, index);
                    if(sub.equals("Snl_Begin")){
                        arrtok.add(new Token(Tokentype.MRBEGIN,start,ligncpt,sub,0));
                        
                    }
                    else if(sub.equals("WHILE")){
                        arrtok.add(new Token(Tokentype.MRWHILE,start+1,ligncpt,sub,0));
                    }
                    else if(sub.equals("End_While")){
                        arrtok.add(new Token(Tokentype.ENDWHILE,start+1,ligncpt,sub,0));
                    }
                    else if(sub.equals("Snl_St")){
                        arrtok.add(new Token(Tokentype.MRST,start+1,ligncpt,sub,0));
                    }
                    else if(sub.equals("Snl_Bool")){
                        arrtok.add(new Token(Tokentype.MRBOOL,start+1,ligncpt,sub,0));
                    }
                    else if(sub.equals("Snl_Add")){
                        arrtok.add(new Token(Tokentype.MRADD,start+1,ligncpt,sub,0));
                    }
                    else if(sub.equals("Snl_Sub")){
                        arrtok.add(new Token(Tokentype.MRSUB,start+1,ligncpt,sub,0));
                    }
                    else if(sub.equals("Snl_Mul")){
                        arrtok.add(new Token(Tokentype.MRMUL,start+1,ligncpt,sub,0));
                    }
                    else if(sub.equals("Snl_Div")){
                        arrtok.add(new Token(Tokentype.MRDIV,start+1,ligncpt,sub,0));
                    }
                    else if(sub.equals("Snl_Int")){
                        arrtok.add(new Token(Tokentype.MRINT,start,ligncpt,sub,0));
                        
                    }
                    else if(sub.equals("Snl_Real")){
                        arrtok.add(new Token(Tokentype.MRREEL,start,ligncpt,sub,0));
                        
                    }
                    else if(sub.equals("Set")){
                        arrtok.add(new Token(Tokentype.MRSET,start,ligncpt,sub,0));
                        
                    }
                    else if(sub.equals("Get")){
                        arrtok.add(new Token(Tokentype.MRGET,start,ligncpt,sub,0));
                        
                    }
                    else if(sub.equals("From")){
                        arrtok.add(new Token(Tokentype.MRFROM,start,ligncpt,sub,0));
                        
                    }
                    else if(sub.equals("Snl_Put")){
                        arrtok.add(new Token(Tokentype.MRPUT,start,ligncpt,sub,0));
                        
                    }
                    else if(sub.equals("If")){
                        arrtok.add(new Token(Tokentype.IF,start,ligncpt,sub,0));
                        
                    }
                    else if(sub.equals("Else")){
                        arrtok.add(new Token(Tokentype.ELSE,start,ligncpt,sub,0));
                        
                    }
                    else if(sub.equals("Snl_End")){
                        arrtok.add(new Token(Tokentype.MREND,start,ligncpt,sub,0));
                        
                    }
                    else if(sub.equals("FALSE")){
                        arrtok.add(new Token(Tokentype.FALSE,start+1,ligncpt,sub,0));
                    }
                    else if(sub.equals("TRUE")){
                        arrtok.add(new Token(Tokentype.TRUE,start+1,ligncpt,sub,0));
                    }
                    else if(sub.equals("End_If")){
                        arrtok.add(new Token(Tokentype.ENDIF,start,ligncpt,sub,0));
                        
                    }
                    else{
                        arrtok.add(new Token(Tokentype.IDENT,start,ligncpt,sub,0));
                        
                    }
                        }
                    }
                    else{
                    sub=lign.substring(start, index);
                    if(sub.equals("Snl_Begin")){
                        arrtok.add(new Token(Tokentype.MRBEGIN,start,ligncpt,sub,0));
                        
                    }
                    else if(sub.equals("WHILE")){
                        arrtok.add(new Token(Tokentype.MRWHILE,start+1,ligncpt,sub,0));
                    }
                    else if(sub.equals("End_While")){
                        arrtok.add(new Token(Tokentype.ENDWHILE,start+1,ligncpt,sub,0));
                    }
                    else if(sub.equals("Snl_Bool")){
                        arrtok.add(new Token(Tokentype.MRBOOL,start+1,ligncpt,sub,0));
                    }
                    else if(sub.equals("Snl_St")){
                        arrtok.add(new Token(Tokentype.MRST,start+1,ligncpt,sub,0));
                    }
                    else if(sub.equals("Snl_Add")){
                        arrtok.add(new Token(Tokentype.MRADD,start+1,ligncpt,sub,0));
                    }
                    else if(sub.equals("Snl_Sub")){
                        arrtok.add(new Token(Tokentype.MRSUB,start+1,ligncpt,sub,0));
                    }
                    else if(sub.equals("Snl_Mul")){
                        arrtok.add(new Token(Tokentype.MRMUL,start+1,ligncpt,sub,0));
                    }
                    else if(sub.equals("Snl_Div")){
                        arrtok.add(new Token(Tokentype.MRDIV,start+1,ligncpt,sub,0));
                    }
                    else if(sub.equals("FALSE")){
                        arrtok.add(new Token(Tokentype.FALSE,start+1,ligncpt,sub,0));
                    }
                    else if(sub.equals("TRUE")){
                        arrtok.add(new Token(Tokentype.TRUE,start+1,ligncpt,sub,0));
                    }
                    else if(sub.equals("Snl_Int")){
                        arrtok.add(new Token(Tokentype.MRINT,start,ligncpt,sub,0));
                        
                    }
                    else if(sub.equals("Snl_Real")){
                        arrtok.add(new Token(Tokentype.MRREEL,start,ligncpt,sub,0));
                        
                    }
                    else if(sub.equals("Set")){
                        arrtok.add(new Token(Tokentype.MRSET,start,ligncpt,sub,0));
                        
                    }
                    else if(sub.equals("Get")){
                        arrtok.add(new Token(Tokentype.MRGET,start,ligncpt,sub,0));
                        
                    }
                    else if(sub.equals("From")){
                        arrtok.add(new Token(Tokentype.MRFROM,start,ligncpt,sub,0));
                        
                    }
                    else if(sub.equals("Snl_Put")){
                        arrtok.add(new Token(Tokentype.MRPUT,start,ligncpt,sub,0));
                        
                    }
                    else if(sub.equals("If")){
                        arrtok.add(new Token(Tokentype.IF,start,ligncpt,sub,0));
                        
                    }
                    else if(sub.equals("Else")){
                        arrtok.add(new Token(Tokentype.ELSE,start,ligncpt,sub,0));
                        
                    }
                    else if(sub.equals("End_If")){
                        arrtok.add(new Token(Tokentype.ENDIF,start,ligncpt,sub,0));
                        
                    }
                    else if(sub.equals("Snl_End")){
                        arrtok.add(new Token(Tokentype.MREND,start,ligncpt,sub,0));
                        
                    }
                    else{
                        arrtok.add(new Token(Tokentype.IDENT,start,ligncpt,sub,0));
                        
                    }
                }}
              else{
                  sub=lign.substring(index, index+1);
                  arrtok.add(new Token(Tokentype.BADTOK,index,ligncpt,sub,0));
                  index++;
              }
            }
        }
        return arrtok;
        
    }
    
}
    public class Parser{
       int index=0;
       int length;
       int err=0;
       int dec;
       int afh;
       int afi;
        ArrayList<String> de = new ArrayList<String>();
       String parstx="";
       String semtx="";
        ArrayList<Token> arrtok;
        public Parser(ArrayList<Token> arr){
            arrtok=arr;
            length=arrtok.size();
            arrtok.set(length-1, new Token(Tokentype.ENDST,-1,-1,"$",-1));
        }
       
        void strg(String ss,String s){
            for(int i =0;i<arrtok.size();i++){
                if(arrtok.get(i).tx.equals(ss)){
                    arrtok.get(i).st=s;
                }
            }
        }
        
        
        void valu(String s,float v){
            for(int i =0;i<arrtok.size();i++){
                if(arrtok.get(i).tx.equals(s)){
                    arrtok.get(i).valeur=v;
                }
            }
        }
        
        boolean utilisable(String s){
            for(String ss : de){
                if(ss.equals(s)){
                    return true;
                }
                
            }
            return false;
        }
        
        void declarat(String s){
            for(int i =0;i<arrtok.size();i++){
                if(arrtok.get(i).tx.equals(s)){
                    arrtok.get(i).declarer=1;
                }
            }
        }
        
        void asignn(String s){
            for(int i =0;i<arrtok.size();i++){
                if(arrtok.get(i).tx.equals(s)){
                    arrtok.get(i).asign=1;
                }
            }
        }
        
        void typage(String s,Tokentype tok){
            for(int i=0;i<arrtok.size();i++){
                if(arrtok.get(i).tx.equals(s)){
                    arrtok.get(i).ty=tok;
                }
            }
        }
        
        
        void parsing(){
            if(arrtok.get(index).type!=Tokentype.MRBEGIN){
                err=1;
                parstx=parstx.concat("error in ligne "+arrtok.get(index).ligne+" position "+arrtok.get(index).pos+" : "+Tokentype.MRBEGIN.toString()+" ( Snl_Begin )"+"Expected but "+arrtok.get(index).type.toString()+" ( "+arrtok.get(index).tx+") was found \n \n");         
            }
            else{
                index++;
                if(arrtok.get(index).type!=Tokentype.FINLIGNE){
                    err=1;
                    parstx=parstx.concat("error in ligne "+arrtok.get(index).ligne+" position "+arrtok.get(index).pos+" : "+Tokentype.FINLIGNE.toString()+" ( \\n )"+"Expected but "+arrtok.get(index).type.toString()+" ( "+arrtok.get(index).tx+") was found \n \n");
                    while(arrtok.get(index).type!=Tokentype.FINLIGNE && arrtok.get(index).type!=Tokentype.ENDST)
                        index++;
                    if(arrtok.get(index).type==Tokentype.ENDST){
                        return ;
                    }
                }
                parstx=parstx.concat(" LIGNE 1 : DEBUT DU PROGRAME \n\n\n");
                index++;
                body();
                for(int i=0;i<length-1;i++){
                    if(arrtok.get(i).type==Tokentype.IDENT){
                        if(arrtok.get(i).declarer==1){
                            if(arrtok.get(i).asign==0){
                                semtx=semtx.concat("ERREUR SEMENTIQUE LIGNE "+arrtok.get(i).ligne+" POSITION "+arrtok.get(i).pos+" : LA VARIABLE "+arrtok.get(i).tx+" NA PAS ETE UTILISER DURANT LE PROGRAME \n\n");
                            }
                        }
                    }
                }
                if(arrtok.get(index-1).type!=Tokentype.MREND){
                    parstx=parstx.concat("ERREUR DANS LIGNE  "+arrtok.get(index).ligne+" position "+arrtok.get(index).pos+" : "+Tokentype.MREND.toString()+" ( Snl_End )"+"Expected but "+arrtok.get(index).type.toString()+" ( "+arrtok.get(index).tx+") was found \n \n");
                }
                else if(err==0){
                    parstx=parstx.concat("L'ANALYSE SYNTAXIQUE A ETE COMPLETER AVEC SUCCER \n");
                }
                else{
                    parstx=parstx.concat("L'ANALYSE SYNTAXIQUE NA PAS  ETE COMPLETER AVEC SUCCER \n");
                }
            }
        }
        
        void body(){
            bodyP();
        }
        
        void bodyP(){
            if(arrtok.get(index).type==Tokentype.ENDST){
                return;
            }
            else{
                ligne();
                bodyP();
            }
        }
        
        void ligne(){
            if(arrtok.get(index).type==Tokentype.FINLIGNE){
                index++;
            }
            else if(arrtok.get(index).type==Tokentype.MRINT||arrtok.get(index).type==Tokentype.MRREEL||arrtok.get(index).type==Tokentype.MRST||arrtok.get(index).type==Tokentype.MRBOOL){
                int ligne =arrtok.get(index).ligne;
                declaration();
                if(arrtok.get(index).type==Tokentype.FINLIGNE){
                    index++;
                    if(dec==1)
                    parstx=parstx.concat("LIGNE "+ligne+" :  DECLARATION D'une ou PLUSIEURS  VARIABLE ENTIER  \n\n ");
                    else if(dec==2)
                        parstx=parstx.concat("LIGNE "+ligne+" :  DECLARATION D'une ou PLUSIEURS  VARIABLE REAL  \n\n ");
                    else if(dec==3)
                        parstx=parstx.concat("LIGNE "+ligne+" :  DECLARATION D'une ou PLUSIEURS  VARIABLE STRING  \n\n ");
                    else
                        parstx=parstx.concat("LIGNE "+ligne+" :  DECLARATION D'une ou PLUSIEURS  VARIABLE BOOLEAN  \n\n ");
                }
                else{
                    err=1;
                    parstx=parstx.concat("error in ligne "+arrtok.get(index).ligne+" position "+arrtok.get(index).pos+" : "+Tokentype.FINLIGNE.toString()+" ( \\n )"+"Expected but "+arrtok.get(index).type.toString()+" ( "+arrtok.get(index).tx+") was found \n \n");
                    while(arrtok.get(index).type!=Tokentype.FINLIGNE && arrtok.get(index).type!=Tokentype.ENDST)
                        index++;
                    if(arrtok.get(index).type==Tokentype.FINLIGNE)
                        index++;
                }
            }
            else if(arrtok.get(index).type==Tokentype.MRBEGIN){
                parstx=parstx.concat("ATTETION  ligne "+arrtok.get(index).ligne+" Snl_Begin doit apparaitre a la premiere ligne mais comme meme cette ligne va etre analyser \n\n");
                index++;
                if(arrtok.get(index).type==Tokentype.FINLIGNE){
                    index++;
                }
                else{
                    err=1;
                    parstx=parstx.concat("error in ligne "+arrtok.get(index).ligne+" position "+arrtok.get(index).pos+" : "+Tokentype.FINLIGNE.toString()+" ( \\n )"+"Expected but "+arrtok.get(index).type.toString()+" ( "+arrtok.get(index).tx+") was found \n \n");
                    while(arrtok.get(index).type!=Tokentype.FINLIGNE && arrtok.get(index).type!=Tokentype.ENDST)
                        index++;
                    if(arrtok.get(index).type==Tokentype.FINLIGNE)
                        index++;
                }
            }
            else if(arrtok.get(index).type==Tokentype.MRADD){
                int ligne=arrtok.get(index).ligne;
                addition();
                if(arrtok.get(index).type==Tokentype.FINLIGNE){
                    index++;
                    parstx=parstx.concat("LIGNE "+ligne+" :  OPERATION D'ADDITION A DEUX ADRESSE  \n\n ");
                }
                else{
                    err=1;
                    parstx=parstx.concat("error in ligne "+arrtok.get(index).ligne+" position "+arrtok.get(index).pos+" : "+Tokentype.FINLIGNE.toString()+" ( \\n )"+"Expected but "+arrtok.get(index).type.toString()+" ( "+arrtok.get(index).tx+") was found \n \n");
                    while(arrtok.get(index).type!=Tokentype.FINLIGNE && arrtok.get(index).type!=Tokentype.ENDST)
                        index++;
                    if(arrtok.get(index).type==Tokentype.FINLIGNE)
                        index++;
                }
            }
            else if(arrtok.get(index).type==Tokentype.MREND){
                index++;
                if(arrtok.get(index).type==Tokentype.ENDST){
                    parstx=parstx.concat("FIN DU PROGRAME \n\n");
                }
                else if(arrtok.get(index).type==Tokentype.FINLIGNE){
                    while(arrtok.get(index).type==Tokentype.FINLIGNE)
                        index++;
                    if(arrtok.get(index).type==Tokentype.ENDST){
                        parstx=parstx.concat("FIN DU PROGRAME \n\n");
                    }
                    else{
                        err=1;
                        parstx=parstx.concat("ERROR ligne "+arrtok.get(index).ligne+" FIN DU PROGRAMME expected but the programe continue");
                    }
                }
                else{
                    err=1;
                    parstx=parstx.concat("error in ligne "+arrtok.get(index).ligne+" position "+arrtok.get(index).pos+" : "+Tokentype.ENDST.toString()+"Expected but "+arrtok.get(index).type.toString()+" ( "+arrtok.get(index).tx+") was found \n \n");
                    while(arrtok.get(index).type!=Tokentype.FINLIGNE && arrtok.get(index).type!=Tokentype.ENDST)
                        index++;
                    if(arrtok.get(index).type==Tokentype.FINLIGNE)
                        index++;
                }
            }
            else if(arrtok.get(index).type==Tokentype.MRSUB){
                int ligne=arrtok.get(index).ligne;
                substraction();
                if(arrtok.get(index).type==Tokentype.FINLIGNE){
                    index++;
                    parstx=parstx.concat("LIGNE "+ligne+" :  OPERATION DE SOUSTRACTION A DEUX ADRESSE  \n\n ");
                }
                else{
                    err=1;
                    parstx=parstx.concat("error in ligne "+arrtok.get(index).ligne+" position "+arrtok.get(index).pos+" : "+Tokentype.FINLIGNE.toString()+" ( \\n )"+"Expected but "+arrtok.get(index).type.toString()+" ( "+arrtok.get(index).tx+") was found \n \n");
                    while(arrtok.get(index).type!=Tokentype.FINLIGNE && arrtok.get(index).type!=Tokentype.ENDST)
                        index++;
                    if(arrtok.get(index).type==Tokentype.FINLIGNE)
                        index++;
                }
            }
            else if(arrtok.get(index).type==Tokentype.MRMUL){
                int ligne=arrtok.get(index).ligne;
                mult();
                if(arrtok.get(index).type==Tokentype.FINLIGNE){
                    index++;
                    parstx=parstx.concat("LIGNE "+ligne+" :  OPERATION DE MULTIPLICATION A DEUX ADRESSE  \n\n ");
                }
                else{
                    err=1;
                    parstx=parstx.concat("error in ligne "+arrtok.get(index).ligne+" position "+arrtok.get(index).pos+" : "+Tokentype.FINLIGNE.toString()+" ( \\n )"+"Expected but "+arrtok.get(index).type.toString()+" ( "+arrtok.get(index).tx+") was found \n \n");
                    while(arrtok.get(index).type!=Tokentype.FINLIGNE && arrtok.get(index).type!=Tokentype.ENDST)
                        index++;
                    if(arrtok.get(index).type==Tokentype.FINLIGNE)
                        index++;
                }
            }
            else if(arrtok.get(index).type==Tokentype.MRDIV){
                int ligne=arrtok.get(index).ligne;
                division();
                if(arrtok.get(index).type==Tokentype.FINLIGNE){
                    index++;
                    parstx=parstx.concat("LIGNE "+ligne+" :  OPERATION DE DIVISION A DEUX ADRESSE  \n\n ");
                }
                else{
                    err=1;
                    parstx=parstx.concat("error in ligne "+arrtok.get(index).ligne+" position "+arrtok.get(index).pos+" : "+Tokentype.FINLIGNE.toString()+" ( \\n )"+"Expected but "+arrtok.get(index).type.toString()+" ( "+arrtok.get(index).tx+") was found \n \n");
                    while(arrtok.get(index).type!=Tokentype.FINLIGNE && arrtok.get(index).type!=Tokentype.ENDST)
                        index++;
                    if(arrtok.get(index).type==Tokentype.FINLIGNE)
                        index++;
                }
            }
            else if(arrtok.get(index).type==Tokentype.MRPUT){
                int ligne=arrtok.get(index).ligne;
                affichage();
                if(arrtok.get(index).type==Tokentype.FINLIGNE){
                    index++;
                    if(afh==1){
                    parstx=parstx.concat("LIGNE "+ligne+" : AFFICHAGE D'UN MESSAGE \n\n ");
                    semtx=semtx.concat("AFFICHAGE DE \" "+arrtok.get(index-4).tx+" \" \n\n");
                    }
                    else{
                        parstx=parstx.concat("LIGNE "+ligne+" : AFFICHAGE D'UNE VARIABLE \n\n ");
                        if(arrtok.get(index-3).ty==Tokentype.STRING&&arrtok.get(index-3).declarer==1&&arrtok.get(index-3).asign==1)
                        semtx=semtx.concat("AFFICHAGE DE \" "+arrtok.get(index-3).st+" \" \n\n");
                        else if(arrtok.get(index-3).declarer==1&&arrtok.get(index-3).asign==1)
                            semtx=semtx.concat("AFFICHAGE DE \" "+Float.toString(arrtok.get(index-3).valeur)+" \" \n\n");
                    }
                }
                else{
                    err=1;
                    parstx=parstx.concat("error in ligne "+arrtok.get(index).ligne+" position "+arrtok.get(index).pos+" : "+Tokentype.FINLIGNE.toString()+" ( \\n )"+"Expected but "+arrtok.get(index).type.toString()+" ( "+arrtok.get(index).tx+") was found \n \n");
                    while(arrtok.get(index).type!=Tokentype.FINLIGNE && arrtok.get(index).type!=Tokentype.ENDST)
                        index++;
                    if(arrtok.get(index).type==Tokentype.FINLIGNE)
                        index++;
                }
            }
            else if(arrtok.get(index).type==Tokentype.MRWHILE){
                int ligne = arrtok.get(index).ligne;
                index++;
                if(arrtok.get(index).type==Tokentype.CRG){
                    index++;
                    condition();
                    if(arrtok.get(index).type==Tokentype.CRD){
                        index++;
                        if(arrtok.get(index).type==Tokentype.FINLIGNE){
                            index++;
                            parstx=parstx.concat("LIGNE "+ligne+" : BOUCLE WHILE  \n\n ");
                            stmt();
                            if(arrtok.get(index).type==Tokentype.ENDWHILE){
                                index++;
                                if(arrtok.get(index).type==Tokentype.FINLIGNE){
                                    index++;
                                }
                                else{
                                    err=1;
                                    parstx=parstx.concat("error in ligne "+arrtok.get(index).ligne+" position "+arrtok.get(index).pos+" : "+Tokentype.FINLIGNE.toString()+" ( \\n )"+"Expected but "+arrtok.get(index).type.toString()+" ( "+arrtok.get(index).tx+") was found \n \n");
                                    while(arrtok.get(index).type!=Tokentype.FINLIGNE && arrtok.get(index).type!=Tokentype.ENDST)
                                        index++;
                                    if(arrtok.get(index).type==Tokentype.FINLIGNE)
                                        index++;
                                }
                            }
                            else{
                                err=1;
                                parstx=parstx.concat("ERROR    : "+Tokentype.ENDIF.toString()+" ( End_While )"+" OF THE WHILE HOW IS IN LIGNE "+ligne+" Expected but "+arrtok.get(index).type.toString()+" ( "+arrtok.get(index).tx+") was found \n \n");
                                while(arrtok.get(index).type!=Tokentype.FINLIGNE && arrtok.get(index).type!=Tokentype.ENDST)
                                    index++;
                                if(arrtok.get(index).type==Tokentype.FINLIGNE)
                                index++;
                            }
                        }
                        else{
                            err=1;
                            parstx=parstx.concat("error in ligne "+arrtok.get(index).ligne+" position "+arrtok.get(index).pos+" : "+Tokentype.FINLIGNE.toString()+" ( \\n )"+"Expected but "+arrtok.get(index).type.toString()+" ( "+arrtok.get(index).tx+") was found \n \n");
                            while(arrtok.get(index).type!=Tokentype.FINLIGNE && arrtok.get(index).type!=Tokentype.ENDST)
                                index++;
                            if(arrtok.get(index).type==Tokentype.FINLIGNE)
                                index++;
                        }
                    }
                    else{
                        err=1;
                        if(arrtok.get(index).type!=Tokentype.FINLIGNE||arrtok.get(index).type==Tokentype.FINLIGNE)
                            parstx=parstx.concat("error in ligne "+arrtok.get(index).ligne+" position "+arrtok.get(index).pos+" : "+Tokentype.CRD.toString()+"( ] ) Expected but "+arrtok.get(index).type.toString()+" ( "+arrtok.get(index).tx+" ) was found \n \n");
                        while(arrtok.get(index).type!=Tokentype.FINLIGNE && arrtok.get(index).type!=Tokentype.ENDST)
                            index++;
                        if(arrtok.get(index).type==Tokentype.FINLIGNE)
                        index++;
                    }
                }
                else{
                    err=1;
                    parstx=parstx.concat("error in ligne "+arrtok.get(index).ligne+" position "+arrtok.get(index).pos+" : "+Tokentype.CRG.toString()+"( [ ) Expected but "+arrtok.get(index).type.toString()+" ( "+arrtok.get(index).tx+" ) was found \n \n");
                    while(arrtok.get(index).type!=Tokentype.FINLIGNE && arrtok.get(index).type!=Tokentype.ENDST)
                        index++;
                    if(arrtok.get(index).type==Tokentype.FINLIGNE)
                        index++;
                }
            }
            else if(arrtok.get(index).type==Tokentype.MRSET || arrtok.get(index).type==Tokentype.MRGET){
                int ligne=arrtok.get(index).ligne;
                affectation();
                if(arrtok.get(index).type==Tokentype.FINLIGNE){
                    index++;
                    if(afi==1)
                    parstx=parstx.concat("LIGNE "+ligne+" : AFFICTATION D'UNE VARIABLE PAR VALEUR  \n\n ");
                    else
                        parstx=parstx.concat("LIGNE "+ligne+" : AFFICTATION D'UNE VARIABLE PAR UNE AUTRE VARIABLE  \n\n ");
                }
                else{
                    err=1;
                    parstx=parstx.concat("error in ligne "+arrtok.get(index).ligne+" position "+arrtok.get(index).pos+" : "+Tokentype.FINLIGNE.toString()+" ( \\n )"+"Expected but "+arrtok.get(index).type.toString()+" ( "+arrtok.get(index).tx+") was found \n \n");
                    while(arrtok.get(index).type!=Tokentype.FINLIGNE && arrtok.get(index).type!=Tokentype.ENDST)
                        index++;
                    if(arrtok.get(index).type==Tokentype.FINLIGNE)
                        index++;
                }
            }
            else if(arrtok.get(index).type==Tokentype.COM){
                int ligne=arrtok.get(index).ligne;
                index++;
                if(arrtok.get(index).type==Tokentype.TEXT){
                    index++;
                    if(arrtok.get(index).type==Tokentype.COM){
                        index++;
                        if(arrtok.get(index).type==Tokentype.FINLIGNE){
                            index++;
                            parstx=parstx.concat("LIGNE "+ligne+" : commentaire \n\n ");
                        }
                        else{
                            err=1;
                            parstx=parstx.concat("error in ligne "+arrtok.get(index).ligne+" position "+arrtok.get(index).pos+" : "+Tokentype.FINLIGNE.toString()+" ( \\n )"+"Expected but "+arrtok.get(index).type.toString()+" ( "+arrtok.get(index).tx+") was found \n \n");
                            while(arrtok.get(index).type!=Tokentype.FINLIGNE && arrtok.get(index).type!=Tokentype.ENDST)
                                index++;
                            if(arrtok.get(index).type==Tokentype.FINLIGNE)
                                index++;
                        }
                    }
                }
                else{
                    err=1;
                    parstx=parstx.concat("error in ligne "+arrtok.get(index).ligne+" position "+arrtok.get(index).pos+" : "+Tokentype.TEXT.toString()+"Expected but "+arrtok.get(index).type.toString()+" was found \n \n");
                    while(arrtok.get(index).type!=Tokentype.FINLIGNE && arrtok.get(index).type!=Tokentype.ENDST)
                        index++;
                    if(arrtok.get(index).type==Tokentype.FINLIGNE)
                        index++;
                }
            }
            else if(arrtok.get(index).type==Tokentype.IF){
                int ligne = arrtok.get(index).ligne;
                index++;
                if(arrtok.get(index).type==Tokentype.CRG){
                    index++;
                    condition();
                    if(arrtok.get(index).type==Tokentype.CRD){
                        index++;
                        if(arrtok.get(index).type==Tokentype.FINLIGNE){
                            index++;
                            parstx=parstx.concat("LIGNE "+ligne+" : IF  \n\n ");
                            stmt();
                            ligneP(ligne);
                            if(arrtok.get(index).type==Tokentype.ENDIF){
                                index++;
                                if(arrtok.get(index).type==Tokentype.FINLIGNE){
                                    index++;
                                }
                                else{
                                    err=1;
                                    parstx=parstx.concat("error in ligne "+arrtok.get(index).ligne+" position "+arrtok.get(index).pos+" : "+Tokentype.FINLIGNE.toString()+" ( \\n )"+"Expected but "+arrtok.get(index).type.toString()+" ( "+arrtok.get(index).tx+") was found \n \n");
                                    while(arrtok.get(index).type!=Tokentype.FINLIGNE && arrtok.get(index).type!=Tokentype.ENDST)
                                        index++;
                                    if(arrtok.get(index).type==Tokentype.FINLIGNE)
                                        index++;
                                }
                            }
                            else{
                                err=1;
                                parstx=parstx.concat("ERROR    : "+Tokentype.ENDIF.toString()+" ( End_If )"+" OF THE IF HOW IS IN LIGNE "+ligne+" Expected but "+arrtok.get(index).type.toString()+" ( "+arrtok.get(index).tx+") was found \n \n");
                                while(arrtok.get(index).type!=Tokentype.FINLIGNE && arrtok.get(index).type!=Tokentype.ENDST)
                                    index++;
                                if(arrtok.get(index).type==Tokentype.FINLIGNE)
                                index++;
                            }
                        }
                        else{
                            err=1;
                            parstx=parstx.concat("error in ligne "+arrtok.get(index).ligne+" position "+arrtok.get(index).pos+" : "+Tokentype.FINLIGNE.toString()+" ( \\n )"+"Expected but "+arrtok.get(index).type.toString()+" ( "+arrtok.get(index).tx+") was found \n \n");
                            while(arrtok.get(index).type!=Tokentype.FINLIGNE && arrtok.get(index).type!=Tokentype.ENDST)
                                index++;
                            if(arrtok.get(index).type==Tokentype.FINLIGNE)
                                index++;
                        }
                    }
                    else{
                        err=1;
                        if(arrtok.get(index).type!=Tokentype.FINLIGNE||arrtok.get(index).type==Tokentype.FINLIGNE)
                            parstx=parstx.concat("error in ligne "+arrtok.get(index).ligne+" position "+arrtok.get(index).pos+" : "+Tokentype.CRD.toString()+"( ] ) Expected but "+arrtok.get(index).type.toString()+" ( "+arrtok.get(index).tx+" ) was found \n \n");
                        while(arrtok.get(index).type!=Tokentype.FINLIGNE && arrtok.get(index).type!=Tokentype.ENDST)
                            index++;
                        if(arrtok.get(index).type==Tokentype.FINLIGNE)
                        index++;
                    }
                }
                else{
                    err=1;
                    parstx=parstx.concat("error in ligne "+arrtok.get(index).ligne+" position "+arrtok.get(index).pos+" : "+Tokentype.CRG.toString()+"( [ ) Expected but "+arrtok.get(index).type.toString()+" ( "+arrtok.get(index).tx+" ) was found \n \n");
                    while(arrtok.get(index).type!=Tokentype.FINLIGNE && arrtok.get(index).type!=Tokentype.ENDST)
                        index++;
                    if(arrtok.get(index).type==Tokentype.FINLIGNE)
                        index++;
                }
            }
            else{
                    err=1;
                    parstx=parstx.concat("error in ligne "+arrtok.get(index).ligne+" position "+arrtok.get(index).pos+" : invalid ipute  "+arrtok.get(index).type.toString()+" ( "+arrtok.get(index).tx+" ) was found \n \n");
                    while(arrtok.get(index).type!=Tokentype.FINLIGNE && arrtok.get(index).type!=Tokentype.ENDST)
                        index++;
                    if(arrtok.get(index).type==Tokentype.FINLIGNE)
                        index++;
            }
            
        }
        void ligneP(int ligneif){
            if(arrtok.get(index).type==Tokentype.ELSE){
                int ligne=arrtok.get(index).ligne;
                index++;
                if(arrtok.get(index).type==Tokentype.FINLIGNE){
                    index++;
                    parstx=parstx.concat("ELSE DU IF QUI SE TROUVE A LA LIGNE : "+ligneif+" \n\n");
                    stmt();
                }
                else{
                    err=1;
                    parstx=parstx.concat("error in ligne "+arrtok.get(index).ligne+" position "+arrtok.get(index).pos+" : "+Tokentype.FINLIGNE.toString()+" ( \\n )"+"Expected but "+arrtok.get(index).type.toString()+" ( "+arrtok.get(index).tx+") was found \n \n");
                            while(arrtok.get(index).type!=Tokentype.FINLIGNE && arrtok.get(index).type!=Tokentype.ENDST)
                                index++;
                            if(arrtok.get(index).type==Tokentype.FINLIGNE)
                                index++;
                }
            }
            else{
                return;
            }
        }
        void stmt(){
            if(arrtok.get(index).type!=Tokentype.ELSE && arrtok.get(index).type!=Tokentype.ENDST && arrtok.get(index).type!=Tokentype.ENDIF && arrtok.get(index).type!=Tokentype.ENDWHILE){
                ligne();
                stmt();
            }
            else{
                return;
            }
        }
        void declaration(){
            if(arrtok.get(index).type==Tokentype.MRINT){
                dec=1;
                index++;
                identi(1);
                if(arrtok.get(index).type==Tokentype.FINIST){
                    index++;
                }
                else{
                    err=1;
                    parstx=parstx.concat("error in ligne "+arrtok.get(index).ligne+" position "+arrtok.get(index).pos+" : "+Tokentype.FINIST.toString()+" ( %. )"+"Expected but "+arrtok.get(index).type.toString()+" ( "+arrtok.get(index).tx+") was found \n \n");
                            while(arrtok.get(index).type!=Tokentype.FINLIGNE && arrtok.get(index).type!=Tokentype.ENDST)
                                index++;
                }
            }
            else if(arrtok.get(index).type==Tokentype.MRREEL){
                dec=2;
                    index++;
                    identi(2);
                    if(arrtok.get(index).type==Tokentype.FINIST){
                     index++;
                    }
                    else{
                            err=1;
                            parstx=parstx.concat("error in ligne "+arrtok.get(index).ligne+" position "+arrtok.get(index).pos+" : "+Tokentype.FINIST.toString()+" ( %. )"+"Expected but "+arrtok.get(index).type.toString()+" ( "+arrtok.get(index).tx+") was found \n \n");
                            while(arrtok.get(index).type!=Tokentype.FINLIGNE && arrtok.get(index).type!=Tokentype.ENDST)
                                index++;
                }
            }
            else if(arrtok.get(index).type==Tokentype.MRST){
                    dec=3;
                    index++;
                    identi(3);
                    if(arrtok.get(index).type==Tokentype.FINIST){
                     index++;
                    }
                    else{
                            err=1;
                            parstx=parstx.concat("error in ligne "+arrtok.get(index).ligne+" position "+arrtok.get(index).pos+" : "+Tokentype.FINIST.toString()+" ( %. )"+"Expected but "+arrtok.get(index).type.toString()+" ( "+arrtok.get(index).tx+") was found \n \n");
                            while(arrtok.get(index).type!=Tokentype.FINLIGNE && arrtok.get(index).type!=Tokentype.ENDST)
                                index++;
                }
            }
            else{
                dec=4;
                    index++;
                    identi(4);
                    if(arrtok.get(index).type==Tokentype.FINIST){
                     index++;
                    }
                    else{
                            err=1;
                            parstx=parstx.concat("error in ligne "+arrtok.get(index).ligne+" position "+arrtok.get(index).pos+" : "+Tokentype.FINIST.toString()+" ( %. )"+"Expected but "+arrtok.get(index).type.toString()+" ( "+arrtok.get(index).tx+") was found \n \n");
                            while(arrtok.get(index).type!=Tokentype.FINLIGNE && arrtok.get(index).type!=Tokentype.ENDST)
                                index++;
                }
            }
        }
        
        void affichage(){
            if(arrtok.get(index).type==Tokentype.MRPUT){
                index++;
                A();
                if(arrtok.get(index).type==Tokentype.FINIST){
                     index++;
                    }
                    else{
                            err=1;
                            parstx=parstx.concat("error in ligne "+arrtok.get(index).ligne+" position "+arrtok.get(index).pos+" : "+Tokentype.FINIST.toString()+" ( %. )"+"Expected but "+arrtok.get(index).type.toString()+" ( "+arrtok.get(index).tx+") was found \n \n");
                            while(arrtok.get(index).type!=Tokentype.FINLIGNE && arrtok.get(index).type!=Tokentype.ENDST)
                                index++;
                }
            }
        }
        
        void A(){
            if(arrtok.get(index).type==Tokentype.DQ){
                afh=1;
                index++;
                if(arrtok.get(index).type==Tokentype.TEXT){
                    index++;
                    if(arrtok.get(index).type==Tokentype.DQ){
                        index++;
                    }
                }
                else{
                    err=1;
                    parstx=parstx.concat("error in ligne "+arrtok.get(index).ligne+" position "+arrtok.get(index).pos+" : "+Tokentype.TEXT.toString()+"Expected but "+arrtok.get(index).type.toString()+" was found \n \n");
                            while(arrtok.get(index).type!=Tokentype.FINLIGNE && arrtok.get(index).type!=Tokentype.ENDST)
                                index++;
                }
            }
            else if(arrtok.get(index).type==Tokentype.IDENT){
                if(arrtok.get(index).declarer==0)
                    semtx=semtx.concat("ERREUR SEMENTIQUE LIGNE "+arrtok.get(index).ligne+" POSITION "+arrtok.get(index).pos+" LA VARIABLE "+arrtok.get(index).tx+" N'A PAS ETE  DECLARER \n\n");
                else if(arrtok.get(index).asign==0)
                    semtx=semtx.concat("ERREUR SEMENTIQUE LIGNE "+arrtok.get(index).ligne+" POSITION "+arrtok.get(index).pos+" LA VARIABLE "+arrtok.get(index).tx+" N'A PAS ETE AFFECTER  \n\n");
                afh=2;
                index++;
            }
            else{
                    err=1;
                    parstx=parstx.concat("error in ligne "+arrtok.get(index).ligne+" position "+arrtok.get(index).pos+" : invalid ipute  "+arrtok.get(index).type.toString()+" ( "+arrtok.get(index).tx+" ) was found \n \n");
                    while(arrtok.get(index).type!=Tokentype.FINLIGNE && arrtok.get(index).type!=Tokentype.ENDST)
                        index++;
            }
        }
        
        void addition(){
            if(arrtok.get(index).type==Tokentype.MRADD){
                index++;
                if(arrtok.get(index).type==Tokentype.IDENT){
                    if(arrtok.get(index).declarer==0)
                        semtx=semtx.concat("ERREUR SEMENTIQUE LIGNE "+arrtok.get(index).ligne+" POSITION "+arrtok.get(index).pos+" LA VARIABLE "+arrtok.get(index).tx+" N'EST PAS DECLARER \n\n");
                     if(arrtok.get(index).ty==Tokentype.BOOL)
                        semtx=semtx.concat("ERREUR SEMENTIQUE LIGNE "+arrtok.get(index).ligne+" POSITION "+arrtok.get(index).pos+" LA VARIABLE "+arrtok.get(index).tx+" EST DE TYPE BOOLEAN \n\n");
                    index++;
                    if(arrtok.get(index).type==Tokentype.VIR){
                        index++;
                        aaa(1);
                        if(arrtok.get(index).type==Tokentype.FINIST){
                            index++;
                        }
                        else{
                            err=1;
                             parstx=parstx.concat("error in ligne "+arrtok.get(index).ligne+" position "+arrtok.get(index).pos+" : "+Tokentype.FINIST.toString()+" ( %. )"+"Expected but "+arrtok.get(index).type.toString()+" ( "+arrtok.get(index).tx+") was found \n \n");
                                while(arrtok.get(index).type!=Tokentype.FINLIGNE && arrtok.get(index).type!=Tokentype.ENDST)
                                    index++;
                        }
                    }
                    else{
                        err=1;
                        parstx=parstx.concat("error in ligne "+arrtok.get(index).ligne+" position "+arrtok.get(index).pos+" : VIR (,) Expected but  "+arrtok.get(index).type.toString()+" ( "+arrtok.get(index).tx+" ) was found \n \n");
                        while(arrtok.get(index).type!=Tokentype.FINLIGNE && arrtok.get(index).type!=Tokentype.ENDST)
                            index++;
                    }
                }
                else{
                    err=1;
                    parstx=parstx.concat("error in ligne "+arrtok.get(index).ligne+" position "+arrtok.get(index).pos+" : invalid ipute  "+arrtok.get(index).type.toString()+" ( "+arrtok.get(index).tx+" ) was found \n \n");
                    while(arrtok.get(index).type!=Tokentype.FINLIGNE && arrtok.get(index).type!=Tokentype.ENDST)
                        index++;
                }
            }
        }
        
        void substraction(){
            if(arrtok.get(index).type==Tokentype.MRSUB){
                index++;
                if(arrtok.get(index).type==Tokentype.IDENT){
                    if(arrtok.get(index).declarer==0)
                        semtx=semtx.concat("ERREUR SEMENTIQUE LIGNE "+arrtok.get(index).ligne+" POSITION "+arrtok.get(index).pos+" LA VARIABLE "+arrtok.get(index).tx+" N'EST PAS DECLARER \n\n");
                     if(arrtok.get(index).ty==Tokentype.TRUE||arrtok.get(index).ty==Tokentype.FALSE)
                        semtx=semtx.concat("ERREUR SEMENTIQUE LIGNE "+arrtok.get(index).ligne+" POSITION "+arrtok.get(index).pos+" LA VARIABLE "+arrtok.get(index).tx+" EST DE TYPE BOOLEAN \n\n");
                    index++;
                    if(arrtok.get(index).type==Tokentype.VIR){
                        index++;
                        aaa(2);
                        if(arrtok.get(index).type==Tokentype.FINIST){
                            index++;
                        }
                        else{
                             err=1;
                             parstx=parstx.concat("error in ligne "+arrtok.get(index).ligne+" position "+arrtok.get(index).pos+" : "+Tokentype.FINIST.toString()+" ( %. )"+"Expected but "+arrtok.get(index).type.toString()+" ( "+arrtok.get(index).tx+") was found \n \n");
                                while(arrtok.get(index).type!=Tokentype.FINLIGNE && arrtok.get(index).type!=Tokentype.ENDST)
                                    index++;
                        }
                    }
                    else{
                        err=1;
                        parstx=parstx.concat("error in ligne "+arrtok.get(index).ligne+" position "+arrtok.get(index).pos+" : VIR (,) Expected but  "+arrtok.get(index).type.toString()+" ( "+arrtok.get(index).tx+" ) was found \n \n");
                        while(arrtok.get(index).type!=Tokentype.FINLIGNE && arrtok.get(index).type!=Tokentype.ENDST)
                            index++;
                    }
                }
                else{
                    err=1;
                    parstx=parstx.concat("error in ligne "+arrtok.get(index).ligne+" position "+arrtok.get(index).pos+" : invalid ipute  "+arrtok.get(index).type.toString()+" ( "+arrtok.get(index).tx+" ) was found \n \n");
                    while(arrtok.get(index).type!=Tokentype.FINLIGNE && arrtok.get(index).type!=Tokentype.ENDST)
                        index++;
                }
            }
        }
        
        void mult(){
            if(arrtok.get(index).type==Tokentype.MRMUL){
                index++;
                if(arrtok.get(index).type==Tokentype.IDENT){
                    if(arrtok.get(index).declarer==0)
                        semtx=semtx.concat("ERREUR SEMENTIQUE LIGNE "+arrtok.get(index).ligne+" POSITION "+arrtok.get(index).pos+" LA VARIABLE "+arrtok.get(index).tx+" N'EST PAS DECLARER \n\n");
                     if(arrtok.get(index).ty==Tokentype.TRUE||arrtok.get(index).ty==Tokentype.FALSE)
                        semtx=semtx.concat("ERREUR SEMENTIQUE LIGNE "+arrtok.get(index).ligne+" POSITION "+arrtok.get(index).pos+" LA VARIABLE "+arrtok.get(index).tx+" EST DE TYPE BOOLEAN \n\n");
                    index++;
                    if(arrtok.get(index).type==Tokentype.VIR){
                        index++;
                        aaa(3);
                        if(arrtok.get(index).type==Tokentype.FINIST){
                            index++;
                        }
                        else{
                             err=1;
                             parstx=parstx.concat("error in ligne "+arrtok.get(index).ligne+" position "+arrtok.get(index).pos+" : "+Tokentype.FINIST.toString()+" ( %. )"+"Expected but "+arrtok.get(index).type.toString()+" ( "+arrtok.get(index).tx+") was found \n \n");
                                while(arrtok.get(index).type!=Tokentype.FINLIGNE && arrtok.get(index).type!=Tokentype.ENDST)
                                    index++;
                        }
                    }
                    else{
                        err=1;
                        parstx=parstx.concat("error in ligne "+arrtok.get(index).ligne+" position "+arrtok.get(index).pos+" : VIR (,) Expected but  "+arrtok.get(index).type.toString()+" ( "+arrtok.get(index).tx+" ) was found \n \n");
                        while(arrtok.get(index).type!=Tokentype.FINLIGNE && arrtok.get(index).type!=Tokentype.ENDST)
                            index++;
                    }
                }
                else{
                    err=1;
                    parstx=parstx.concat("error in ligne "+arrtok.get(index).ligne+" position "+arrtok.get(index).pos+" : invalid ipute  "+arrtok.get(index).type.toString()+" ( "+arrtok.get(index).tx+" ) was found \n \n");
                    while(arrtok.get(index).type!=Tokentype.FINLIGNE && arrtok.get(index).type!=Tokentype.ENDST)
                        index++;
                }
            }
        }
        
        void division(){
            if(arrtok.get(index).type==Tokentype.MRDIV){
                index++;
                if(arrtok.get(index).type==Tokentype.IDENT){
                    if(arrtok.get(index).declarer==0)
                        semtx=semtx.concat("ERREUR SEMENTIQUE LIGNE "+arrtok.get(index).ligne+" POSITION "+arrtok.get(index).pos+" LA VARIABLE "+arrtok.get(index).tx+" N'EST PAS DECLARER \n\n");
                    if(arrtok.get(index).ty==Tokentype.TRUE||arrtok.get(index).ty==Tokentype.FALSE)
                        semtx=semtx.concat("ERREUR SEMENTIQUE LIGNE "+arrtok.get(index).ligne+" POSITION "+arrtok.get(index).pos+" LA VARIABLE "+arrtok.get(index).tx+" EST DE TYPE BOOLEAN \n\n");
                    index++;
                    if(arrtok.get(index).type==Tokentype.VIR){
                        index++;
                        aaa(4);
                        if(arrtok.get(index).type==Tokentype.FINIST){
                            index++;
                        }
                        else{
                             err=1;
                             parstx=parstx.concat("error in ligne "+arrtok.get(index).ligne+" position "+arrtok.get(index).pos+" : "+Tokentype.FINIST.toString()+" ( %. )"+"Expected but "+arrtok.get(index).type.toString()+" ( "+arrtok.get(index).tx+") was found \n \n");
                                while(arrtok.get(index).type!=Tokentype.FINLIGNE && arrtok.get(index).type!=Tokentype.ENDST)
                                    index++;
                        }
                    }
                    else{
                        err=1;
                        parstx=parstx.concat("error in ligne "+arrtok.get(index).ligne+" position "+arrtok.get(index).pos+" : VIR (,) Expected but  "+arrtok.get(index).type.toString()+" ( "+arrtok.get(index).tx+" ) was found \n \n");
                        while(arrtok.get(index).type!=Tokentype.FINLIGNE && arrtok.get(index).type!=Tokentype.ENDST)
                            index++;
                    }
                }
                else{
                    err=1;
                    parstx=parstx.concat("error in ligne "+arrtok.get(index).ligne+" position "+arrtok.get(index).pos+" : invalid ipute  "+arrtok.get(index).type.toString()+" ( "+arrtok.get(index).tx+" ) was found \n \n");
                    while(arrtok.get(index).type!=Tokentype.FINLIGNE && arrtok.get(index).type!=Tokentype.ENDST)
                        index++;
                }
            }
        }
        
        void aaa(int c){
            if(arrtok.get(index).type==Tokentype.IDENT){
                if(arrtok.get(index).declarer==0)
                    semtx=semtx.concat("ERREUR SEMENTIQUE LIGNE "+arrtok.get(index).ligne+" POSITION "+arrtok.get(index).pos+" LA VARIABLE "+arrtok.get(index).tx+" N'A PAS ETE DECLARER \n\n");
                if(arrtok.get(index).asign==0)
                    semtx=semtx.concat("ERREUR SEMENTIQUE LIGNE "+arrtok.get(index).ligne+" POSITION "+arrtok.get(index).pos+" LA VARIABLE "+arrtok.get(index).tx+" N'A PAS ETE AFFECTER \n\n");
                else if(arrtok.get(index-2).declarer==1 && arrtok.get(index-2).asign==1){
                if(arrtok.get(index-2).ty!=arrtok.get(index).ty){
                    if(c==1)
                    semtx=semtx.concat("ERREUR SEMENTIQUE LIGNE "+arrtok.get(index).ligne+" POSITION "+arrtok.get(index).pos+" ADDITION ENTRE DEUX VARIABLE INCOMPATIBLE   LA VARIABLE "+arrtok.get(index-2).tx+" POSITION "+arrtok.get(index-2).pos+" QUI EST DE TYPE "+arrtok.get(index-2).ty.toString()+" ET LA VARIABLE "+arrtok.get(index).tx+" POSITION "+arrtok.get(index).pos+" QUI EST DE TYPE "+arrtok.get(index).ty.toString()+"  \n\n");
                    else if(c==2)
                        semtx=semtx.concat("ERREUR SEMENTIQUE LIGNE "+arrtok.get(index).ligne+" POSITION "+arrtok.get(index).pos+" SOUSTRACTION ENTRE DEUX VARIABLE INCOMPATIBLE   LA VARIABLE "+arrtok.get(index-2).tx+" POSITION "+arrtok.get(index-2).pos+" QUI EST DE TYPE "+arrtok.get(index-2).ty.toString()+" ET LA VARIABLE "+arrtok.get(index).tx+" POSITION "+arrtok.get(index).pos+" QUI EST DE TYPE "+arrtok.get(index).ty.toString()+"  \n\n");
                    else if(c==3)
                        semtx=semtx.concat("ERREUR SEMENTIQUE LIGNE "+arrtok.get(index).ligne+" POSITION "+arrtok.get(index).pos+" MULTIPLICATION ENTRE DEUX VARIABLE INCOMPATIBLE   LA VARIABLE "+arrtok.get(index-2).tx+" POSITION "+arrtok.get(index-2).pos+" QUI EST DE TYPE "+arrtok.get(index-2).ty.toString()+" ET LA VARIABLE "+arrtok.get(index).tx+" POSITION "+arrtok.get(index).pos+" QUI EST DE TYPE "+arrtok.get(index).ty.toString()+"  \n\n");
                    else
                        semtx=semtx.concat("ERREUR SEMENTIQUE LIGNE "+arrtok.get(index).ligne+" POSITION "+arrtok.get(index).pos+" DIVISION ENTRE DEUX VARIABLE INCOMPATIBLE   LA VARIABLE "+arrtok.get(index-2).tx+" POSITION "+arrtok.get(index-2).pos+" QUI EST DE TYPE "+arrtok.get(index-2).ty.toString()+" ET LA VARIABLE "+arrtok.get(index).tx+" POSITION "+arrtok.get(index).pos+" QUI EST DE TYPE "+arrtok.get(index).ty.toString()+"  \n\n");
                    
                }
                else if(arrtok.get(index-2).declarer==1 && arrtok.get(index-2).asign==1){
                if(arrtok.get(index-2).ty==Tokentype.STRING){
                    if(c==2)
                    semtx=semtx.concat("ERREUR SEMENTIQUE LIGNE "+arrtok.get(index).ligne+" POSITION "+arrtok.get(index).pos+" SOUSTRACTION ENTRE DEUX VARIABLE DE TYPE STRING   LA VARIABLE "+arrtok.get(index-2).tx+" POSITION "+arrtok.get(index-2).pos+" QUI EST DE TYPE "+arrtok.get(index-2).ty.toString()+" ET LA VARIABLE "+arrtok.get(index).tx+" POSITION "+arrtok.get(index).pos+" QUI EST DE TYPE "+arrtok.get(index).ty.toString()+"  \n\n");
                    else if(c==3)
                        semtx=semtx.concat("ERREUR SEMENTIQUE LIGNE "+arrtok.get(index).ligne+" POSITION "+arrtok.get(index).pos+" MULTIPLICATION  ENTRE DEUX VARIABLE DE TYPE STRING   LA VARIABLE "+arrtok.get(index-2).tx+" POSITION "+arrtok.get(index-2).pos+" QUI EST DE TYPE "+arrtok.get(index-2).ty.toString()+" ET LA VARIABLE "+arrtok.get(index).tx+" POSITION "+arrtok.get(index).pos+" QUI EST DE TYPE "+arrtok.get(index).ty.toString()+"  \n\n");
                    else if(c==4)
                        semtx=semtx.concat("ERREUR SEMENTIQUE LIGNE "+arrtok.get(index).ligne+" POSITION "+arrtok.get(index).pos+" DIVISION ENTRE DEUX VARIABLE DE TYPE STRING   LA VARIABLE "+arrtok.get(index-2).tx+" POSITION "+arrtok.get(index-2).pos+" QUI EST DE TYPE "+arrtok.get(index-2).ty.toString()+" ET LA VARIABLE "+arrtok.get(index).tx+" POSITION "+arrtok.get(index).pos+" QUI EST DE TYPE "+arrtok.get(index).ty.toString()+"  \n\n");
                }
                if(c==4){
                    if(arrtok.get(index).valeur==0)
                        semtx=semtx.concat("ERREUR SEMENTIQUE LIGNE "+arrtok.get(index).ligne+" POSITION "+arrtok.get(index).pos+" DIVISION PAR ZERO CAR LA VARIABLE "+arrtok.get(index).tx+" EGALE ZERO \n\n");
                }}
                }
                index++;
            }
            else if(arrtok.get(index).type==Tokentype.ENTIER){
               if(arrtok.get(index-2).declarer==1&&arrtok.get(index-2).asign==1){
               if(arrtok.get(index-2).ty!=Tokentype.ENTIER){
                    if(c==1)
                    semtx=semtx.concat("ERREUR SEMENTIQUE LIGNE "+arrtok.get(index).ligne+" POSITION "+arrtok.get(index).pos+" ADDITION ENTRE DEUX VARIABLE INCOMPATIBLE   LA VARIABLE "+arrtok.get(index-2).tx+" POSITION "+arrtok.get(index-2).pos+" QUI EST DE TYPE "+arrtok.get(index-2).ty.toString()+" ET LA VARIABLE "+arrtok.get(index).tx+" POSITION "+arrtok.get(index).pos+" QUI EST DE TYPE "+arrtok.get(index).ty.toString()+"  \n\n");
                    else if(c==2)
                        semtx=semtx.concat("ERREUR SEMENTIQUE LIGNE "+arrtok.get(index).ligne+" POSITION "+arrtok.get(index).pos+" SOUSTRACTION ENTRE DEUX VARIABLE INCOMPATIBLE   LA VARIABLE "+arrtok.get(index-2).tx+" POSITION "+arrtok.get(index-2).pos+" QUI EST DE TYPE "+arrtok.get(index-2).ty.toString()+" ET LA VARIABLE "+arrtok.get(index).tx+" POSITION "+arrtok.get(index).pos+" QUI EST DE TYPE "+arrtok.get(index).ty.toString()+"  \n\n");
                    else if(c==3)
                        semtx=semtx.concat("ERREUR SEMENTIQUE LIGNE "+arrtok.get(index).ligne+" POSITION "+arrtok.get(index).pos+" MULTIPLICATION ENTRE DEUX VARIABLE INCOMPATIBLE   LA VARIABLE "+arrtok.get(index-2).tx+" POSITION "+arrtok.get(index-2).pos+" QUI EST DE TYPE "+arrtok.get(index-2).ty.toString()+" ET LA VARIABLE "+arrtok.get(index).tx+" POSITION "+arrtok.get(index).pos+" QUI EST DE TYPE "+arrtok.get(index).ty.toString()+"  \n\n");
                    else
                        semtx=semtx.concat("ERREUR SEMENTIQUE LIGNE "+arrtok.get(index).ligne+" POSITION "+arrtok.get(index).pos+" DIVISION ENTRE DEUX VARIABLE INCOMPATIBLE   LA VARIABLE "+arrtok.get(index-2).tx+" POSITION "+arrtok.get(index-2).pos+" QUI EST DE TYPE "+arrtok.get(index-2).ty.toString()+" ET LA VARIABLE "+arrtok.get(index).tx+" POSITION "+arrtok.get(index).pos+" QUI EST DE TYPE "+arrtok.get(index).ty.toString()+"  \n\n");
               }
               if(c==4){
                    if(arrtok.get(index).valeur==0)
                        semtx=semtx.concat("ERREUR SEMENTIQUE LIGNE "+arrtok.get(index).ligne+" POSITION "+arrtok.get(index).pos+" DIVISION PAR ZERO  EGALE ZERO \n\n");
                }
               }
                index++;
            
            }
            else if(arrtok.get(index).type==Tokentype.REAL){
                if(arrtok.get(index-2).declarer==1&&arrtok.get(index-2).asign==1){
                 if(arrtok.get(index-2).type!=Tokentype.REAL){
                    if(c==1)
                    semtx=semtx.concat("ERREUR SEMENTIQUE LIGNE "+arrtok.get(index).ligne+" POSITION "+arrtok.get(index).pos+" ADDITION ENTRE DEUX VARIABLE INCOMPATIBLE   LA VARIABLE "+arrtok.get(index-2).tx+" POSITION "+arrtok.get(index-2).pos+" QUI EST DE TYPE "+arrtok.get(index-2).ty.toString()+" ET LA VARIABLE "+arrtok.get(index).tx+" POSITION "+arrtok.get(index).pos+" QUI EST DE TYPE "+arrtok.get(index).ty.toString()+"  \n\n");
                    else if(c==2)
                        semtx=semtx.concat("ERREUR SEMENTIQUE LIGNE "+arrtok.get(index).ligne+" POSITION "+arrtok.get(index).pos+" SOUSTRACTION ENTRE DEUX VARIABLE INCOMPATIBLE   LA VARIABLE "+arrtok.get(index-2).tx+" POSITION "+arrtok.get(index-2).pos+" QUI EST DE TYPE "+arrtok.get(index-2).ty.toString()+" ET LA VARIABLE "+arrtok.get(index).tx+" POSITION "+arrtok.get(index).pos+" QUI EST DE TYPE "+arrtok.get(index).ty.toString()+"  \n\n");
                    else if(c==3)
                        semtx=semtx.concat("ERREUR SEMENTIQUE LIGNE "+arrtok.get(index).ligne+" POSITION "+arrtok.get(index).pos+" MULTIPLICATION ENTRE DEUX VARIABLE INCOMPATIBLE   LA VARIABLE "+arrtok.get(index-2).tx+" POSITION "+arrtok.get(index-2).pos+" QUI EST DE TYPE "+arrtok.get(index-2).ty.toString()+" ET LA VARIABLE "+arrtok.get(index).tx+" POSITION "+arrtok.get(index).pos+" QUI EST DE TYPE "+arrtok.get(index).ty.toString()+"  \n\n");
                    else
                        semtx=semtx.concat("ERREUR SEMENTIQUE LIGNE "+arrtok.get(index).ligne+" POSITION "+arrtok.get(index).pos+" DIVISION ENTRE DEUX VARIABLE INCOMPATIBLE   LA VARIABLE "+arrtok.get(index-2).tx+" POSITION "+arrtok.get(index-2).pos+" QUI EST DE TYPE "+arrtok.get(index-2).ty.toString()+" ET LA VARIABLE "+arrtok.get(index).tx+" POSITION "+arrtok.get(index).pos+" QUI EST DE TYPE "+arrtok.get(index).ty.toString()+"  \n\n");
               }
               if(c==4){
                    if(arrtok.get(index).valeur==0)
                        semtx=semtx.concat("ERREUR SEMENTIQUE LIGNE "+arrtok.get(index).ligne+" POSITION "+arrtok.get(index).pos+" DIVISION PAR ZERO  \n\n");
                }
                }
                index++;
            }
            else{
                err=1;
                parstx=parstx.concat("error in ligne "+arrtok.get(index).ligne+" position "+arrtok.get(index).pos+" : invalid ipute (IDENT or ENTIER or REAL) was Expected but   "+arrtok.get(index).type.toString()+" ( "+arrtok.get(index).tx+" ) was found \n \n");
                    while(arrtok.get(index).type!=Tokentype.FINLIGNE && arrtok.get(index).type!=Tokentype.ENDST)
                        index++;
            }
        }
        
        void affectation(){
            if(arrtok.get(index).type==Tokentype.MRSET){
                afi=1;
                index++;
                if(arrtok.get(index).type==Tokentype.IDENT){
                    System.out.println(arrtok.get(index).tx+" "+arrtok.get(index).declarer+"\n");
                    if(arrtok.get(index).declarer==0){
                        semtx=semtx.concat("ERREUR SEMENTIQUE LIGNE "+arrtok.get(index).ligne+" POSITION "+arrtok.get(index).pos+" LA VARIABLE "+arrtok.get(index).tx+" N'EST PAS DECLARER \n\n");
                    }
                    index++;
                    aff();
                    if(arrtok.get(index).type==Tokentype.FINIST){
                        index++;
                    }
                    else{
                        err=1;
                        parstx=parstx.concat("error in ligne "+arrtok.get(index).ligne+" position "+arrtok.get(index).pos+" : "+Tokentype.FINIST.toString()+" ( %. )"+"Expected but "+arrtok.get(index).type.toString()+" ( "+arrtok.get(index).tx+") was found \n \n");
                            while(arrtok.get(index).type!=Tokentype.FINLIGNE && arrtok.get(index).type!=Tokentype.ENDST)
                                index++;
                    }
                }
                else{
                    err=1;
                    parstx=parstx.concat("error in ligne "+arrtok.get(index).ligne+" position "+arrtok.get(index).pos+" : invalid ipute  "+arrtok.get(index).type.toString()+" ( "+arrtok.get(index).tx+" ) was found \n \n");
                    while(arrtok.get(index).type!=Tokentype.FINLIGNE && arrtok.get(index).type!=Tokentype.ENDST)
                        index++;
                }
            }
            else if(arrtok.get(index).type==Tokentype.MRGET){
                afi=2;
                index++;
                if(arrtok.get(index).type==Tokentype.IDENT){
                    if(arrtok.get(index).declarer==0)
                        semtx=semtx.concat("ERREUR SEMENTIQUE LIGNE "+arrtok.get(index).ligne+" POSITION "+arrtok.get(index).pos+" LA VARIABLE "+arrtok.get(index).tx+" N'EST PAS DECLARER \n\n");

                    index++;
                    if(arrtok.get(index).type==Tokentype.MRFROM){
                        index++;
                        if(arrtok.get(index).type==Tokentype.IDENT){
                            if(arrtok.get(index).declarer==0)
                                semtx=semtx.concat("ERREUR SEMENTIQUE LIGNE "+arrtok.get(index).ligne+" POSITION "+arrtok.get(index).pos+" LA VARIABLE "+arrtok.get(index).tx+" NA  PAS ETE DECLARER \n\n");
                            if(arrtok.get(index).asign==0)
                                semtx=semtx.concat("ERREUR SEMENTIQUE LIGNE "+arrtok.get(index).ligne+" POSITION "+arrtok.get(index).pos+" LA VARIABLE "+arrtok.get(index).tx+" NA PAS ETE AFFECTER \n\n");
                            if(arrtok.get(index-2).declarer==1 && arrtok.get(index).declarer==1 && arrtok.get(index).asign==1){
                            if(arrtok.get(index).ty!=arrtok.get(index-2).ty)
                                semtx=semtx.concat("ERREUR SEMENTIQUE LIGNE "+arrtok.get(index).ligne+" POSITION "+arrtok.get(index).pos+" AFFECTATION DE LA VARIABLE "+arrtok.get(index).tx+" DE TYPE "+arrtok.get(index).ty.toString()+" A LA VARIABLE "+arrtok.get(index-2).tx+" QUI EST DE TYPE "+arrtok.get(index-2).ty.toString()+" INCOMPATIBILITE DES TYPE \n\n");

                            else{
                                if(arrtok.get(index-2).type==Tokentype.STRING){
                                    arrtok.get(index-2).st=arrtok.get(index).st;
                                    arrtok.get(index-2).asign=1;
                                    asignn(arrtok.get(index-2).tx);
                                }
                                else{
                                    arrtok.get(index-2).valeur=arrtok.get(index).valeur;
                                    arrtok.get(index-2).asign=1;
                                    asignn(arrtok.get(index-2).tx);
                                    valu(arrtok.get(index-2).tx,arrtok.get(index-2).valeur);
                                }
                            }
                            }
                            index++;
                            if(arrtok.get(index).type==Tokentype.FINIST){
                             index++;
                            }
                             else{
                                err=1;
                                    parstx=parstx.concat("error in ligne "+arrtok.get(index).ligne+" position "+arrtok.get(index).pos+" : "+Tokentype.FINIST.toString()+" ( %. )"+"Expected but "+arrtok.get(index).type.toString()+" ( "+arrtok.get(index).tx+") was found \n \n");
                                     while(arrtok.get(index).type!=Tokentype.FINLIGNE && arrtok.get(index).type!=Tokentype.ENDST)
                                        index++;
                                }
                             }
                    else{
                            err=1;
                        parstx=parstx.concat("error in ligne "+arrtok.get(index).ligne+" position "+arrtok.get(index).pos+" : invalid ipute  "+arrtok.get(index).type.toString()+" ( "+arrtok.get(index).tx+" ) was found \n \n");
                        while(arrtok.get(index).type!=Tokentype.FINLIGNE && arrtok.get(index).type!=Tokentype.ENDST)
                            index++;
                    }
                    }
                    else{
                        err=1;
                        parstx=parstx.concat("error in ligne "+arrtok.get(index).ligne+" position "+arrtok.get(index).pos+" : "+Tokentype.MRFROM+"was Expected but "+arrtok.get(index).type.toString()+" ( "+arrtok.get(index).tx+" ) was found \n \n");
                        while(arrtok.get(index).type!=Tokentype.FINLIGNE && arrtok.get(index).type!=Tokentype.ENDST)
                            index++;
                    }
                    
                }
                else{
                    err=1;
                    parstx=parstx.concat("error in ligne "+arrtok.get(index).ligne+" position "+arrtok.get(index).pos+" : invalid ipute  "+arrtok.get(index).type.toString()+" ( "+arrtok.get(index).tx+" ) was found \n \n");
                    while(arrtok.get(index).type!=Tokentype.FINLIGNE && arrtok.get(index).type!=Tokentype.ENDST)
                        index++;
                }
            }
        }
        
        void aff(){
            if(arrtok.get(index).type==Tokentype.ENTIER){
                if(arrtok.get(index-1).declarer==1){
                if(arrtok.get(index-1).ty!=Tokentype.ENTIER)
                    semtx=semtx.concat("ERREUR SEMENTIQUE LIGNE "+arrtok.get(index).ligne+" POSITION "+arrtok.get(index).pos+" AFFECTATION D'UNE VALEUR ENTIER A LA VARIABLE "+arrtok.get(index-1).tx+" QUI EST DE TYPE "+arrtok.get(index-1).ty.toString()+" \n\n");
                else{
                 arrtok.get(index-1).valeur=Float.parseFloat(arrtok.get(index).tx);
                    valu(arrtok.get(index-1).tx, arrtok.get(index-1).valeur);
                 arrtok.get(index-1).asign=1;
                 asignn(arrtok.get(index-1).tx);
                 }
                }
                index++;
            }
            else if(arrtok.get(index).type==Tokentype.REAL){
                if(arrtok.get(index-1).declarer==1){
                 if(arrtok.get(index-1).ty!=Tokentype.REAL)
                    semtx=semtx.concat("ERREUR SEMENTIQUE LIGNE "+arrtok.get(index).ligne+" POSITION "+arrtok.get(index).pos+" AFFECTATION D'UNE VALEUR REAL A LA VARIABLE "+arrtok.get(index-1).tx+" QUI EST DE TYPE "+arrtok.get(index-1).ty.toString()+" \n\n");
                 else{
                 arrtok.get(index-1).valeur=Float.parseFloat(arrtok.get(index).tx);
                 valu(arrtok.get(index-1).tx, arrtok.get(index-1).valeur);
                 arrtok.get(index-1).asign=1;
                 asignn(arrtok.get(index-1).tx);
                 }
                }
                index++;
            }
            else if(arrtok.get(index).type==Tokentype.DQ){
                index++;
                if(arrtok.get(index).type==Tokentype.TEXT){
                    index++;
                    if(arrtok.get(index).type==Tokentype.DQ){
                        if(arrtok.get(index-3).declarer==1){
                         if(arrtok.get(index-3).ty!=Tokentype.STRING){
                            semtx=semtx.concat("ERREUR SEMENTIQUE LIGNE "+arrtok.get(index).ligne+" POSITION "+arrtok.get(index).pos+" AFFECTATION D'UNE CHAINE DE CARACTERE A LA VARIABLE "+arrtok.get(index-3).tx+" QUI EST DE TYPE "+arrtok.get(index-3).ty.toString()+" \n\n");
                         }
                         else{
                                arrtok.get(index-3).st=arrtok.get(index-1).tx;
                                strg(arrtok.get(index-3).tx,arrtok.get(index-3).st);
                                arrtok.get(index-3).asign=1;
                                asignn(arrtok.get(index-3).tx);
                         }
                        }
                        index++;
                    }
                }
                else{
                    err=1;
                    parstx=parstx.concat("ERREUR DANS LIGNE  "+arrtok.get(index).ligne+" POSITION "+arrtok.get(index).pos+" : "+Tokentype.TEXT.toString()+"EXEPTED MAIS  "+arrtok.get(index).type.toString()+" A eté TROUVER \n \n");
                            while(arrtok.get(index).type!=Tokentype.FINLIGNE && arrtok.get(index).type!=Tokentype.ENDST)
                                index++;
                }
            
            }
            else if(arrtok.get(index).type==Tokentype.TRUE){
                if(arrtok.get(index-1).declarer==1){
                 if(arrtok.get(index-1).ty!=Tokentype.TRUE)
                    semtx=semtx.concat("ERREUR SEMENTIQUE LIGNE "+arrtok.get(index).ligne+" POSITION "+arrtok.get(index).pos+" AFFECTATION D'UNE VALEUR BOOLEAN A LA VARIABLE "+arrtok.get(index-1).tx+" QUI EST DE TYPE "+arrtok.get(index-1).ty.toString()+" \n\n");
                 else{
                 arrtok.get(index-1).valeur=1;
                 arrtok.get(index-1).asign=1;
                 }
                }
                index++;
            }
            else if(arrtok.get(index).type==Tokentype.FALSE){
                if(arrtok.get(index-1).declarer==1){
                 if(arrtok.get(index-1).ty!=Tokentype.FALSE)
                    semtx=semtx.concat("ERREUR SEMENTIQUE LIGNE "+arrtok.get(index).ligne+" POSITION "+arrtok.get(index).pos+" AFFECTATION D'UNE VALEUR BOOLEAN A LA VARIABLE "+arrtok.get(index-1).tx+" QUI EST DE TYPE "+arrtok.get(index-1).ty.toString()+" \n\n");
                 else{
                 arrtok.get(index-1).valeur=0;
                 arrtok.get(index-1).asign=1;
                 }
                }
                index++;
            }
            else{
                err=1;
                parstx=parstx.concat("error in ligne "+arrtok.get(index).ligne+" position "+arrtok.get(index).pos+" : "+Tokentype.ENTIER.toString()+" or "+Tokentype.REAL.toString()+" Expected"+arrtok.get(index).type.toString()+" ( "+arrtok.get(index).tx+" ) was found \n \n");
                    while(arrtok.get(index).type!=Tokentype.FINLIGNE && arrtok.get(index).type!=Tokentype.ENDST)
                        index++;
            }
        }
        
        void condition(){
            exp();
            condP();
        }
        
        void condP(){
            if(arrtok.get(index).type==Tokentype.EQEQ || arrtok.get(index).type==Tokentype.NEQ || arrtok.get(index).type==Tokentype.GTE||arrtok.get(index).type==Tokentype.GT||arrtok.get(index).type==Tokentype.LTE||arrtok.get(index).type==Tokentype.LT){
                op();
                condition();
            }
            else if(arrtok.get(index).type==Tokentype.AND || arrtok.get(index).type==Tokentype.OR){
                opl();
                condition();
            }
            else{
                return;
            }
        }
        
        void exp(){
            if(arrtok.get(index).type==Tokentype.IDENT){
                if(arrtok.get(index).declarer==0)
                    semtx=semtx.concat("ERREUR SEMENTIQUE LIGNE "+arrtok.get(index).ligne+" POSITION "+arrtok.get(index).pos+" LA VARIABLE "+arrtok.get(index).tx+" N'EST PAS DECLARER \n\n");
                else{
                    if(arrtok.get(index).asign==0)
                        semtx=semtx.concat("ERREUR SEMENTIQUE LIGNE "+arrtok.get(index).ligne+" POSITION "+arrtok.get(index).pos+" LA VARIABLE "+arrtok.get(index).tx+" N'EST PAS ETE AFFECTER \n\n");
                    else
                        if(arrtok.get(index).ty==Tokentype.STRING)
                            semtx=semtx.concat("ERREUR SEMENTIQUE LIGNE "+arrtok.get(index).ligne+" POSITION "+arrtok.get(index).pos+" LA VARIABLE "+arrtok.get(index).tx+" EST DE TYPE STRING ELLE NE PEUT PAS ETRE DANS UNE CONDITION \n\n");
                }
                index++;
            }
            else if(arrtok.get(index).type==Tokentype.ENTIER){
                index++;
            }
            else if(arrtok.get(index).type==Tokentype.REAL){
                index++;
            }
            else if(arrtok.get(index).type==Tokentype.FALSE){
                index++;
            }
            else if(arrtok.get(index).type==Tokentype.TRUE){
                index++;
            }
            else{
                err=1;
                parstx=parstx.concat("error in ligne "+arrtok.get(index).ligne+" position "+arrtok.get(index).pos+" : IDENT or"+Tokentype.ENTIER.toString()+" or "+Tokentype.REAL.toString()+" or FALSE or TRUE  Expected but "+arrtok.get(index).type.toString()+" ( "+arrtok.get(index).tx+" ) was found \n \n");
                    while(arrtok.get(index).type!=Tokentype.FINLIGNE && arrtok.get(index).type!=Tokentype.ENDST)
                        index++;
            }
        }
        void op(){
            index++;
        }
        
        void opl(){
            index++;
        }
        
        void identi(int d){
            if(arrtok.get(index).type==Tokentype.IDENT){
                
                if(!utilisable(arrtok.get(index).tx)){
                arrtok.get(index).declarer=1;
                    declarat(arrtok.get(index).tx);
                    System.out.println(arrtok.get(index).tx+" "+arrtok.get(index).declarer+" \n");
                de.add(arrtok.get(index).tx);
                if(d==1){
                    arrtok.get(index).ty=Tokentype.ENTIER;
                    typage(arrtok.get(index).tx, Tokentype.ENTIER);
                }
                else if(d==2){
                    arrtok.get(index).ty=Tokentype.REAL;
                    typage(arrtok.get(index).tx, Tokentype.REAL);
                }
                else if(d==3){
                    arrtok.get(index).ty=Tokentype.STRING;
                    typage(arrtok.get(index).tx, Tokentype.STRING);
                }
                else {
                    arrtok.get(index).ty=Tokentype.BOOL;
                    typage(arrtok.get(index).tx, Tokentype.BOOL);
                }
                }
                else
                    semtx=semtx.concat("ERREUR SEMENTIQUE LIGNE "+arrtok.get(index).ligne+" POSITION "+arrtok.get(index).pos+" : LA VARIABLE "+arrtok.get(index).tx+" EST DEJA DECLARER \n\n");
                index++;
                Aident(d);
            }
            else{
                err=1;
                parstx=parstx.concat("error in ligne "+arrtok.get(index).ligne+" position "+arrtok.get(index).pos+" : IDENT  Expected but "+arrtok.get(index).type.toString()+" ( "+arrtok.get(index).tx+" ) was found \n \n");
                    while(arrtok.get(index).type!=Tokentype.FINLIGNE && arrtok.get(index).type!=Tokentype.ENDST)
                        index++;
            }
        }
        
        void Aident(int d){
            if(arrtok.get(index).type==Tokentype.VIR){
                index++;
                identi(d);
            }
            else{
                return;
            }
        }
        
        
    }
    
    public class sementique{
        
    }
    
    


    public Homepage() {
        initComponents();
        textare.setEditable(false);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        btn_lex = new javax.swing.JButton();
        btn_sem = new javax.swing.JButton();
        btn_synt = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        textare = new javax.swing.JTextArea();
        btn_fichier = new javax.swing.JButton();
        label_fichier = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        btn_lex.setIcon(new javax.swing.ImageIcon(getClass().getResource("/abc.png"))); // NOI18N
        btn_lex.setText("Lexique");
        btn_lex.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_lexActionPerformed(evt);
            }
        });

        btn_sem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/brain.png"))); // NOI18N
        btn_sem.setText("Sémentique");
        btn_sem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_semActionPerformed(evt);
            }
        });

        btn_synt.setText("Syntaxe");
        btn_synt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_syntActionPerformed(evt);
            }
        });

        textare.setColumns(20);
        textare.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        textare.setRows(5);
        jScrollPane2.setViewportView(textare);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2)
                .addGap(33, 33, 33))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addComponent(btn_lex, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(176, 176, 176)
                .addComponent(btn_synt, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 216, Short.MAX_VALUE)
                .addComponent(btn_sem, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(49, 49, 49))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_lex, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_synt, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_sem, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 357, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14))
        );

        btn_fichier.setText("Charger fichier");
        btn_fichier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_fichierActionPerformed(evt);
            }
        });

        label_fichier.setBackground(new java.awt.Color(255, 255, 255));
        label_fichier.setText("jLabel2");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addComponent(btn_fichier, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(label_fichier, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_fichier, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label_fichier, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel2);
        jPanel2.setBounds(0, 0, 980, 480);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_fichierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_fichierActionPerformed
        // TODO add your handling code here:
            openfile = new JFileChooser();
            openfile.setFileFilter(new FileNameExtensionFilter("snail files","snail"));
            int res = openfile.showOpenDialog(this);
            if(res== JFileChooser.APPROVE_OPTION){
                label_fichier.setText(openfile.getSelectedFile().getAbsolutePath());
                fichier= new File(openfile.getSelectedFile().getAbsolutePath());
            }
            
    }//GEN-LAST:event_btn_fichierActionPerformed

    private void btn_lexActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_lexActionPerformed
        try {
            // TODO add your handling code here:
            Lexer lex=new Lexer();
            ArrayList<Token> arrtok;
            arrtok=lex.tokenizer(new File(label_fichier.getText()));
            textare.setText("");
            for(Token tok : arrtok){
                if("\n".equals(tok.tx))
                    textare.append("\\n"+" :   ligine "+tok.ligne+"  position "+tok.pos+" valeur  "+tok.valeur+" token "+tok.type.toString()+"\n\n");
                else
                    textare.append(tok.tx+" : ligine "+tok.ligne+" position "+tok.pos+" valeur  "+tok.valeur+" token "+tok.type.toString()+"\n\n");
            }
        } catch (IOException ex) {
            Logger.getLogger(Homepage.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }//GEN-LAST:event_btn_lexActionPerformed

    private void btn_syntActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_syntActionPerformed
        // TODO add your handling code here:
        try {
            Lexer lex=new Lexer();
            ArrayList<Token> arrtok;
            arrtok=lex.tokenizer(new File(label_fichier.getText()));
            textare.setText("");
            Parser parse= new Parser(arrtok);
            parse.parsing();
            textare.append(parse.parstx);
        } catch (IOException ex) {
            Logger.getLogger(Homepage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btn_syntActionPerformed

    private void btn_semActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_semActionPerformed
        // TODO add your handling code here:
        try {
            Lexer lex=new Lexer();
            ArrayList<Token> arrtok;
            arrtok=lex.tokenizer(new File(label_fichier.getText()));
            textare.setText("");
            Parser parse= new Parser(arrtok);
            parse.parsing();
            textare.append(parse.semtx);
        } catch (IOException ex) {
            Logger.getLogger(Homepage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btn_semActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Homepage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Homepage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Homepage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Homepage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Homepage().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_fichier;
    private javax.swing.JButton btn_lex;
    private javax.swing.JButton btn_sem;
    private javax.swing.JButton btn_synt;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel label_fichier;
    private javax.swing.JTextArea textare;
    // End of variables declaration//GEN-END:variables
}
