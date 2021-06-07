package project.shop.portfolio.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.shop.portfolio.domain.Cart;
import project.shop.portfolio.domain.Customer;
import project.shop.portfolio.domain.item.ItemDetail;
import project.shop.portfolio.dto.CartDTO;
import project.shop.portfolio.dto.OrderFromDTO;
import project.shop.portfolio.repository.CartRepository;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {
    private final CustomerService customerService;
    private final CartRepository cartRepository;
    private final ItemService itemService;

    public List<Cart> getCartList(String userId){
        Optional<List<Cart>> result = cartRepository.findAllByUserId(userId);
        if (result.isPresent()){
            return result.get();
        }
        return null;
    }

    public void addCartList(Cart cart){
        cartRepository.save(cart);
    }
    public void deleteCartList(Cart cart){
        cartRepository.delete(cart);
    }

    public void addCart(String userId, String optionId, int quantity) {
        ItemDetail itemDetail= itemService.getItemOptionByItemDetailId(Long.valueOf(optionId));
        Cart cart= Cart.builder()
                .customer(customerService.findByUserid(userId))
                .itemDetail(itemDetail)
                .price(itemDetail.getParentItem().getPrice())
                .quantity(quantity)
                .build();
        cartRepository.save(cart);
    }

    public List<CartDTO> getCartDTOList(String userId) {
        List<CartDTO> cartDTOList = new ArrayList<>();
        List<Cart> cartList = getCartList(userId);
        for (Cart cart : cartList) {
            CartDTO cartDTO = CartDTO.builder()
                    .cartId(String.valueOf(cart.getId()))
                    .itemDetailId(String.valueOf(cart.getItemDetail().getId()))
                    .quantity(cart.getQuantity())
                    .price(cart.getPrice())
                    .itemName(cart.getItemDetail().getParentItem().getName())
                    .size(cart.getItemDetail().getSize())
                    .color(cart.getItemDetail().getColor())
                    .th_ImgName(cart.getItemDetail().getParentItem().getItemImageList().get(0).getThumbnailFileName())
                    .build();
            cartDTOList.add(cartDTO);
        }

        return cartDTOList;
    }

    public void deleteCartByCartId(Long id) {
        cartRepository.deleteById(id);
    }

    public OrderFromDTO getOrderFormDTO(String itemDetail_id, String p_size, String pp_name, String p_price, String w_quantity, String pp_thumb, String p_color) {
        OrderFromDTO orderFromDTO = OrderFromDTO.builder()
                .itemDetail_id(Long.valueOf(itemDetail_id))
                .size(p_size)
                .itemName(pp_name)
                .price(Integer.valueOf(p_price))
                .quantity(Integer.valueOf(w_quantity))
                .thumbImgName(pp_thumb)
                .color(p_color)
                .build();
        return orderFromDTO;

    }

    public void deleteCartByCartId(String userId, Long cartId) {
        Customer byUserid = customerService.findByUserid(userId);
        List<Cart> carts = byUserid.getCarts();
        for (Cart cart : carts) {
            if(cart.getId() == cartId){
                cartRepository.deleteById(cartId);
            }

        }
    }
}
