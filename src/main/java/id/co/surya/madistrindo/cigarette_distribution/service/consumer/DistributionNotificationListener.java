//package id.co.surya.madistrindo.cigarette_distribution.service.consumer;
//
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Component;
//
//@Component
//public class DistributionNotificationListener {
//
//    @KafkaListener(topics = "new-distribution", groupId = "distribution-group")
//    public void listenNewDistribution(String message) {
//        System.out.println("📦 [NOTIF] Distribusi Baru: " + message);
//    }
//}
