package com.uca.pncparcialfinalrestaurante.service.impl;

import com.uca.pncparcialfinalrestaurante.dto.PedidoItemRequest;
import com.uca.pncparcialfinalrestaurante.dto.PedidoRequest;
import com.uca.pncparcialfinalrestaurante.entity.*;
import com.uca.pncparcialfinalrestaurante.entity.enums.EstadoPedido;
import com.uca.pncparcialfinalrestaurante.repository.*;
import com.uca.pncparcialfinalrestaurante.service.PedidoService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepository;
    private final UsuarioRepository usuarioRepository;
    private final MesaRepository mesaRepository;
    private final ProductoRepository productoRepository;

    public PedidoServiceImpl(PedidoRepository pedidoRepository,
                             UsuarioRepository usuarioRepository,
                             MesaRepository mesaRepository,
                             ProductoRepository productoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.usuarioRepository = usuarioRepository;
        this.mesaRepository = mesaRepository;
        this.productoRepository = productoRepository;
    }

    @Override
    public Pedido crearPedido(PedidoRequest request) {
        var cliente = usuarioRepository.findById(request.getClienteId())
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));
        var mesa = mesaRepository.findById(request.getMesaId())
                .orElseThrow(() -> new IllegalArgumentException("Mesa no encontrada"));

        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setMesa(mesa);
        pedido.setEstado(EstadoPedido.CREADO);
        pedido.setCreatedAt(LocalDateTime.now());

        BigDecimal total = BigDecimal.ZERO;

        for (PedidoItemRequest itemReq : request.getItems()) {
            var producto = productoRepository.findById(itemReq.getProductoId())
                    .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado: " + itemReq.getProductoId()));
            PedidoItem item = new PedidoItem();
            item.setProducto(producto);
            item.setCantidad(itemReq.getCantidad());
            item.setPrecioUnitario(producto.getPrecio());
            item.setPedido(pedido);
            pedido.getItems().add(item);

            BigDecimal itemTotal = producto.getPrecio().multiply(BigDecimal.valueOf(item.getCantidad()));
            total = total.add(itemTotal);
        }

        pedido.setTotal(total);
        return pedidoRepository.save(pedido);
    }

    @Override
    public Pedido obtenerPorId(Long id) {
        return pedidoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado"));
    }

    @Override
    public List<Pedido> obtenerTodos() {
        return pedidoRepository.findAll();
    }

    @Override
    public List<Pedido> obtenerPorSucursal(Long sucursalId) {
        return pedidoRepository.findByMesaSucursalId(sucursalId);
    }

    @Override
    public List<Pedido> obtenerPorCliente(Long clienteId) {
        return pedidoRepository.findByClienteId(clienteId);
    }

    @Override
    public Pedido cancelarPedido(Long id) {
        Pedido p = obtenerPorId(id);

        // Validar autorización: CLIENTE solo puede cancelar su propio pedido
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_CLIENTE"))) {
            String username = auth.getName();
            Usuario usuario = usuarioRepository.findByUsername(username)
                    .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
            if (!Objects.equals(p.getCliente().getId(), usuario.getId())) {
                throw new AccessDeniedException("No autorizado para cancelar pedido de otro cliente");
            }
        }

        p.setEstado(EstadoPedido.CANCELADO);
        return pedidoRepository.save(p);
    }

    @Override
    public Pedido confirmarPedido(Long id) {
        Pedido p = obtenerPorId(id);

        // Validar autorización: ENCARGADO solo puede confirmar pedidos de su sucursal
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ENCARGADO"))) {
            String username = auth.getName();
            Usuario usuario = usuarioRepository.findByUsername(username)
                    .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
            Long pedidoBranchId = p.getMesa().getSucursal().getId();
            Long userBranchId = usuario.getSucursal().getId();
            if (!Objects.equals(pedidoBranchId, userBranchId)) {
                throw new AccessDeniedException("No autorizado: pedido pertenece a otra sucursal");
            }
        }

        p.setEstado(EstadoPedido.CONFIRMADO);
        return pedidoRepository.save(p);
    }
}