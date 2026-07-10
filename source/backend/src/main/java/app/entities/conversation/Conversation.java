//package app.entities.conversation;
//
//import jakarta.persistence.*;
//
//@Entity
//@Table(name = "conversations")
//public class Conversation {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//    @ManyToOne
//    @JoinColumn(name = "product_id",nullable = false)
//    private Long productId;
//    @ManyToOne
//    @JoinColumn(name = "buyer_id",nullable = false)
//    private  Long buyerId;
//    @ManyToOne
//    @JoinColumn(name = "seller_id")
//    private Long sellerId;
//    public Conversation(Long productId,Long buyerId,Long sellerId){
//        this.buyerId=buyerId;
//        this.productId=productId;
//        this.sellerId=sellerId;
//    }
//
//    public Long getId() {
//        return id;
//    }
//
//    public Long getBuyerId() {
//        return buyerId;
//    }
//
//    public Long getProductId() {
//        return productId;
//    }
//
//    public Long getSellerId() {
//        return sellerId;
//    }
//
//    public void setBuyerId(Long buyerId) {
//        this.buyerId = buyerId;
//    }
//
//    public void setProductId(Long productId) {
//        this.productId = productId;
//    }
//
//    public void setSellerId(Long sellerId) {
//        this.sellerId = sellerId;
//    }
//}
