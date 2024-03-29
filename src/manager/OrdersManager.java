package src.manager;

import org.apache.poi.ss.usermodel.*;
import src.model.Clothes;
import src.model.Orders;
import src.model.PhotoDecal;
import src.model.Product;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;


import static src.manager.ClothesManager.scanner;

public class OrdersManager {
    ArrayList<Orders> orders = new ArrayList<>();

    private ProductManager productManager;

    public OrdersManager(ProductManager productManager) {
        this.productManager = productManager;
    }

    public void setOrders(ArrayList<Orders> orders){
         this.orders = orders;
    }
    public ArrayList<Orders> getOrders(){
        return this.orders;
    }
    public void add(Orders orders){
        this.orders.add(orders);
    }
    public Orders get(int i){
            return this.orders.get(i);
    }
    public void display(){
        for (Orders order:
             orders) {
            System.out.println(order);
        }
    }
    public void displayByCode(){
        System.out.println("Nhập mã của đơn hàng muốn tìm: ");
        String code = scanner.nextLine();
        for (Orders order:
             orders) {
            if(order.getCode().equals(code)){
                System.out.println(order);
                break;
            }
        }
    }
    public long profitPerOrder(ArrayList<Product> product){
        long capital = 0,revenue = 0;
        for(int j = 0 ; j < product.size();j++){
            capital += product.get(j).getPrice();
            revenue += product.get(j).getClothes().getPrice() + product.get(j).getPhotoDecal().getPrice();
        }
        return revenue - capital;
    }
    public void displayProfitEO(){
        for (int i = 0; i < orders.size(); i++) {
            ArrayList<Product> product = orders.get(i).getProducts();
            System.out.println("lợi nhuận của đơn " + orders.get(i).getCode()+":"+ profitPerOrder(product));
        }
    }
    public void displayProfitAO(){
        long profit = 0;
        for (int i = 0; i < orders.size(); i++) {
            ArrayList<Product> product = orders.get(i).getProducts();
            profit += profitPerOrder(product);
        }
        System.out.println("lợi nhuận của tất cả các đơn:"+ profit);
    }
    public void minusQuantityC(Product product,ArrayList<Clothes> cloth) {
        for (int i = 0; i < cloth.size(); i++) {
            if (product.getName().equals(cloth.get(i))) {
                cloth.get(i).setQuantity(product.getQuantity() - cloth.get(i).getQuantity());
                break;
            }
        }
    }
    public void minusQuantityD(Product product,ArrayList<PhotoDecal> decal){
        for(int i = 0 ; i < decal.size();i++){
            if(product.getName().equals(decal.get(i))){
                decal.get(i).setQuantity(product.getQuantity() - decal.get(i).getQuantity());
                break;
            }
        }
    }
    public void displayNeedProduct(ClothesManager clothesManager,DecalManager decalManager){
        ProductManager productManager1 = new ProductManager(clothesManager,decalManager);

        for(int i = 0 ; i < orders.size(); i++){
            ArrayList<Product> product = orders.get(i).getProducts();
            for(int  j  = 0 ; j < product.size();j++){
               if(productManager1.findByCode(product.get(j).getName()) == -1){
                   productManager1.add(product.get(j));
               }
               else{
                   productManager1.get(productManager1.findByCode(product.get(j).getName())).setQuantity(productManager1.get(productManager1.findByCode(product.get(j).getName())).getQuantity()+1);
               }
            }
        }
        ArrayList<Clothes> tempClothes = new ArrayList<>();
        ArrayList<PhotoDecal> tempDecals = new ArrayList<>();
        for(int i = 0 ; i < productManager1.products.size();i++){
            tempClothes = addCLothes(tempClothes,productManager1.get(i).getClothes(),productManager1.get(i).getQuantity());
            tempDecals = addDecal(tempDecals,productManager1.get(i).getPhotoDecal(),productManager1.get(i).getQuantity());
        }
        System.out.println("===========ÁO TRƠN================");
        for (int i = 0 ; i < tempClothes.size();i++){
            System.out.println(tempClothes.get(i).getName() +","+tempClothes.get(i).getQuantity());
        }
        System.out.println("===========Hình================");
        for (int i = 0 ; i < tempDecals.size();i++){
            System.out.println(tempDecals.get(i).getName() +","+tempDecals.get(i).getQuantity());
        }
    }
    public ArrayList<PhotoDecal> addDecal(ArrayList<PhotoDecal> decals,PhotoDecal decal,Long quantiy){
        for(int  i = 0 ; i < decals.size();i++){
            if(decals.get(i).equals(decal)){
                decals.get(i).setQuantity(decals.get(i).getQuantity() + quantiy);
                return decals;
            }
        }
        PhotoDecal decal1 = decal;
        decal1.setQuantity(quantiy);
        decals.add(decal1);
        return decals;
    }
    public ArrayList<Clothes> addCLothes(ArrayList<Clothes> clothes,Clothes cloth,Long quanity){
        for(int  i = 0 ; i < clothes.size();i++){
            if(clothes.get(i).equals(cloth)){
                clothes.get(i).setQuantity(clothes.get(i).getQuantity() + quanity);
                return clothes;
            }
        }
        Clothes clothes1 = cloth;
        clothes1.setQuantity(quanity);
        clothes.add(clothes1);
        return clothes;
    }
    public void displayLackOrders(ClothesManager clothesManager,DecalManager decalManager){
        for(int i = 0 ; i < orders.size(); i++){
             ArrayList<Product> product = orders.get(i).getProducts();
             for(int  j  = 0 ; j < product.size();j++){
                 minusQuantityD(product.get(j),decalManager.photos);
                 minusQuantityC(product.get(j),clothesManager.clothes);
             }
        }
        System.out.println("======== Áo còn thiếu ============");
        for(int i = 0 ; i < clothesManager.clothes.size();i++){
            if(clothesManager.clothes.get(i).getQuantity() < 0){
                System.out.println(clothesManager.clothes.get(i).getName()+":"+(-clothesManager.clothes.get(i).getQuantity()));
            }
        }
        System.out.println("========= Hình còn thiếu ==========");
        for(int i = 0 ; i < decalManager.photos.size();i++){
            if(decalManager.photos.get(i).getQuantity() < 0){
                System.out.println(clothesManager.clothes.get(i).getName()+":"+(-clothesManager.clothes.get(i).getQuantity()));
            }
        }
    }
    public int FindbyCode(String code){
        for (int i = 0; i < orders.size(); i++) {
            if(orders.get(i).getCode().equals(code))
                return i;
        }
        return -1;
    }

    public void read(){
         String file_name = "TEST.xlsx";
         File file = new File(file_name);
        Workbook workbook;
        FileInputStream fins = null;
        try {
            fins = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            workbook = WorkbookFactory.create(fins);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Sheet sheet = workbook.getSheetAt(0);
        FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
        for (Row row: sheet){
            int count = 1;
            StringBuffer code = new StringBuffer();
            StringBuffer name = new StringBuffer();
            StringBuffer color = new StringBuffer();
            Long quantity = null;
            for (Cell cell: row){
                CellValue cellValue = formulaEvaluator.evaluate(cell);
                String Value= cellValue.getStringValue();
                if(count == 1){
                    code.append(Value);
                    if(FindbyCode(code.toString()) == -1)
                        orders.add(new Orders(code.toString(),null,null,new ArrayList<Product>()));
                }
                if(count == 6){
                    name.append(Value);
                }
                else if(count  == 7){
                    color.append(Value);
                    if(orders.get(FindbyCode(code.toString())).checkProduct(name.toString())){
                        Product product = productManager.get(productManager.FindByNameVar(name.toString(),color.toString()));
                        product.setQuantity(0);
                        orders.get(FindbyCode(code.toString())).add(product);
                    }
                }
                else if(count == 8){
                    System.out.println("code:"+code+"name:"+name);
                    quantity = Long.parseLong(Value);
                    orders.get(FindbyCode(code.toString())).setQuantity(orders.get(FindbyCode(code.toString())).getProduct(name.toString()),quantity);
                }
                if(count == 9)
                    continue;
                count++;
            }
            System.out.println(orders.get(FindbyCode(code.toString())));
        }
    }
}
