import com.order.ServicesOrderApplication;
import com.products.pojo.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.client.RestTemplate;

@SpringBootTest(classes = ServicesOrderApplication.class)
public class LoadBalancerTest {

    @Autowired
    LoadBalancerClient loadBalancerClient;

    @Autowired
    RestTemplate restTemplate;
    @Test
    void BalancerClientTest(){
        for (int i = 0; i < 12; i++) {
            ServiceInstance choose=loadBalancerClient.choose("services-products");
            System.out.println("choose =" +choose.getHost()+": "+choose.getPort());
        }
    }

    @Test
    void loadBalanceTest(){
        for (int i = 0; i < 12; i++) {
            String url="http://services-products/product/10";
            //2.给远程发送请求   service-products(微服务名字)会被动态替换
            Product product=restTemplate.getForObject(url,Product.class);
            System.out.println(product);
        }

    }


}