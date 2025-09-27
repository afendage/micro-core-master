import com.alibaba.cloud.nacos.discovery.NacosServiceDiscovery;
import com.alibaba.nacos.api.exception.NacosException;
import com.products.ServicesProductsApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import java.util.List;

@SpringBootTest(classes = ServicesProductsApplication.class)
public class DiscoveryTest {

    @Autowired
    DiscoveryClient discoveryClient;
    @Autowired
    NacosServiceDiscovery nacosServiceDiscovery;
    @Test
    void  nacosServiceDiscoveryTest() throws NacosException {
        for (String service : nacosServiceDiscovery.getServices()){
            System.out.println("service="+service);
            //获取ip+port
            List<ServiceInstance> instances=nacosServiceDiscovery.getInstances(service);
            for (ServiceInstance instance : instances){
                System.out.println("ip:"+instance.getHost()+";"+" post="+instance.getPort());
            }

        }
    }
    @Test
    void discoveryClientTesr(){
        for (String service : discoveryClient.getServices()){
            System.out.println("service="+service);
            //获取ip+port
            List<ServiceInstance> instances=discoveryClient.getInstances(service);
            for (ServiceInstance instance : instances){
                System.out.println("ip:"+instance.getHost()+";"+" post="+instance.getPort());
            }
        }
    }

}
