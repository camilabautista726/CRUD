package co.edu.uniquindio.poo.empresajfx.viewController;

import co.edu.uniquindio.poo.empresajfx.App;
import co.edu.uniquindio.poo.empresajfx.controller.ProductoController;
import co.edu.uniquindio.poo.empresajfx.model.Producto;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class ProductoViewController {

    ProductoController productoController;
    ObservableList<Producto> listProductos = FXCollections.observableArrayList();
    Producto selectedProducto;
    private App app;

    @FXML
    private TextField txtCodigo;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtPrecio;
    @FXML
    private TableView<Producto> tblListProducto;
    @FXML
    private TableColumn<Producto, String> tbcCodigo;
    @FXML
    private TableColumn<Producto, String> tbcNombre;
    @FXML
    private TableColumn<Producto, String> tbcPrecio;

    @FXML
    void onAgregarProducto() {
        agregarProducto();
    }

    @FXML
    void onActualizarProducto() {
        actualizarProducto();
    }

    @FXML
    void onLimpiar() {
        limpiarSeleccion();
    }

    @FXML
    void onEliminar() {
        eliminarProducto();
    }

    @FXML
    void initialize() {
        productoController = new ProductoController(App.empresa);
        initView();
    }

    private void initView() {
        initDataBinding();
        listProductos.addAll(productoController.obtenerListaProductos());
        tblListProducto.setItems(listProductos);
        listenerSelection();
    }

    private void initDataBinding() {
        tbcCodigo.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCodigo()));
        tbcNombre.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNombre()));
        tbcPrecio.setCellValueFactory(c -> new SimpleStringProperty(String.valueOf(c.getValue().getPrecio())));
    }

    private void listenerSelection() {
        tblListProducto.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            selectedProducto = newSelection;
            mostrarInformacionProducto(selectedProducto);
        });
    }

    private void mostrarInformacionProducto(Producto producto) {
        if (producto != null) {
            txtCodigo.setText(producto.getCodigo());
            txtNombre.setText(producto.getNombre());
            txtPrecio.setText(String.valueOf(producto.getPrecio()));
        }
    }

    private Producto buildProducto() {
        try {
            double precio = Double.parseDouble(txtPrecio.getText());
            return new Producto(txtCodigo.getText(), txtNombre.getText(), precio);
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "El precio debe ser un número").show();
            return null;
        }
    }

    private void agregarProducto() {
        Producto producto = buildProducto();
        if (producto != null && productoController.crearProducto(producto)) {
            listProductos.add(producto);
            limpiarCamposProducto();
        }
    }

    private void eliminarProducto() {
        if (selectedProducto != null && productoController.eliminarProducto(selectedProducto.getCodigo())) {
            listProductos.remove(selectedProducto);
            limpiarSeleccion();
        }
    }

    private void actualizarProducto() {
        if (selectedProducto != null) {
            Producto nuevo = buildProducto();
            if (nuevo != null && productoController.actualizarProducto(selectedProducto.getCodigo(), nuevo)) {
                listProductos.setAll(productoController.obtenerListaProductos());
                limpiarSeleccion();
            }
        }
    }

    private void limpiarSeleccion() {
        tblListProducto.getSelectionModel().clearSelection();
        limpiarCamposProducto();
    }

    private void limpiarCamposProducto() {
        txtCodigo.clear();
        txtNombre.clear();
        txtPrecio.clear();
    }

    public void setApp(App app) {
        this.app = app;
    }
}
