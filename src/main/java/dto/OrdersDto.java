package dto;


import com.zgj.reggie.entity.OrderDetail;
import com.zgj.reggie.entity.Orders;
import lombok.Data;
import java.util.List;

@Data
public class OrdersDto extends Orders {

    private String userName;

    private String phone;

    private String address;
    //以上使用userId查询user表获取

    private String consignee;

    private List<OrderDetail> orderDetails;
	
}
