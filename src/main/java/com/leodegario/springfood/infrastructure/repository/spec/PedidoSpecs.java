package com.leodegario.springfood.infrastructure.repository.spec;

import com.leodegario.springfood.domain.model.Pedido;
import com.leodegario.springfood.domain.model.Restaurante;
import com.leodegario.springfood.repository.filter.PedidoFilter;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.util.ArrayList;

public class PedidoSpecs {

   public static Specification<Pedido> usandoFiltro(PedidoFilter filtro){
       return (root, query, builder) -> {
           root.fetch("restaurante").fetch("cozinha");
           root.fetch("cliente");

           var predicates = new ArrayList<Predicate>();

           if(filtro.getClienteId() != null){
               predicates.add(builder.equal(root.get("cliente"), filtro.getClienteId()));
           }

           if(filtro.getRestauranteId() != null){
               predicates.add(builder.equal(root.get("restaurante"), filtro.getRestauranteId()));
           }

           if(filtro.getDataCriacaoInicio() != null){
               predicates.add(builder.greaterThanOrEqualTo(root.get("dataCriacao"), filtro.getDataCriacaoInicio()));
           }

           if(filtro.getDataCriacaoFim() != null){
               predicates.add(builder.lessThanOrEqualTo(root.get("dataCriacao"), filtro.getDataCriacaoFim()));
           }

           return builder.and(predicates.toArray(new Predicate[0]));
       };
   }
}
