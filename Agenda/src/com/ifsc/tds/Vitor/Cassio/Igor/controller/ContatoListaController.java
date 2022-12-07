package com.ifsc.tds.Vitor.Cassio.Igor.controller;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import com.ifsc.tds.Vitor.Cassio.Igor.Entity.Contato;
import com.ifsc.tds.Vitor.Cassio.Igor.dao.ContatoDAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.scene.Scene;

public class ContatoListaController implements Initializable {

    @FXML
    private TableView<Contato> tbvContato;

    @FXML
    private TableColumn<Contato, Long> tbcCodigo;

    @FXML
    private TableColumn<Contato, String> tbcNome;

    @FXML
    private Label lblNomeContato;

    @FXML
    private Label lblTelefoneContato;

    @FXML
    private Label lblEmailContato;

    @FXML
    private Button btnIncluir;

    @FXML
    private Button btnEditar;

    @FXML
    private Button btnExcluir;
    
    private List<Contato> listaContatos;
	private ObservableList<Contato> observableListaContatos = FXCollections.observableArrayList();
	private ContatoDAO ContatoDAO;

	public static final String CONTATO_EDITAR = " - Editar";
	public static final String CONTATO_INCLUIR = " - Incluir";

    @FXML
    void onClickBtnEditar(ActionEvent event) {	
    	Contato Contato = this.tbvContato.getSelectionModel().getSelectedItem();

		if (Contato != null) {
			boolean btnConfirmarClic = this.onShowTelaContatoEditar(Contato, ContatoListaController.CONTATO_EDITAR);

			if (btnConfirmarClic) {
				this.getContatoDAO().update(Contato, null);
				this.carregarTableViewContatos();
			}
		} else {
			Alert alerta = new Alert(Alert.AlertType.ERROR);
			alerta.setContentText("Por favor, escolha uma Contato na tabela!");
			alerta.show();
		}
	}

    @FXML
    void onClickBtnExcluir(ActionEvent event) {
    	Contato Contato = this.tbvContato.getSelectionModel().getSelectedItem();

		if (Contato != null) {

			Alert alerta = new Alert(AlertType.CONFIRMATION);
			alerta.setTitle("Pergunta");
			alerta.setHeaderText("Confirma a exclusão do Contato?\n" + Contato.getNome());

			ButtonType botaoNao = ButtonType.NO;
			ButtonType botaoSim = ButtonType.YES;
			alerta.getButtonTypes().setAll(botaoSim, botaoNao);
			Optional<ButtonType> resultado = alerta.showAndWait();

			if (resultado.get() == botaoSim) {
				this.getContatoDAO().delete(Contato);
				this.carregarTableViewContatos();
			}
		} else {
			Alert alerta = new Alert(Alert.AlertType.ERROR);
			alerta.setContentText("Por favor, escolha uma Contato na tabela!");
			alerta.show();
		}
	}

    @FXML
    void onClickBtnIncluir(ActionEvent event) {
    	Contato Contato = new Contato();

		boolean btnConfirmarClic = this.onShowTelaContatoEditar(Contato, ContatoListaController.CONTATO_INCLUIR);

		if (btnConfirmarClic) {
			this.getContatoDAO().save(Contato);
			this.carregarTableViewContatos();
		}
	}

	public ContatoDAO getContatoDAO() {
		return ContatoDAO;
	}

	public void setContatoDAO(ContatoDAO ContatoDAO) {
		this.ContatoDAO = ContatoDAO;
	}

	public List<Contato> getListaContatos() {
		return listaContatos;
	}

	public void setListaContatos(List<Contato> listaContatos) {
		this.listaContatos = listaContatos;
	}

	public ObservableList<Contato> getObservableListaContatos() {
		return observableListaContatos;
	}

	public void setObservableListaContatos(ObservableList<Contato> observableListaContato) {
		this.observableListaContatos = observableListaContato;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.setContatoDAO(new ContatoDAO());
		this.carregarTableViewContatos();
		this.selecionarItemTableViewContatos(null);

		this.tbvContato.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> selecionarItemTableViewContatos(newValue));
		
	}
	
	public void carregarTableViewContatos() {
		this.tbcCodigo.setCellValueFactory(new PropertyValueFactory<>("id"));
		this.tbcNome.setCellValueFactory(new PropertyValueFactory<>("nome"));

		this.setListaContatos(this.getContatoDAO().getAll());
		this.setObservableListaContatos(FXCollections.observableArrayList(this.getListaContatos()));
		this.tbvContato.setItems(this.getObservableListaContatos());
	}

	public void selecionarItemTableViewContatos(Contato Contato) {
		if (Contato != null) {
			this.lblNomeContato.setText(Contato.getNome());
			this.lblTelefoneContato.setText(Contato.getTelefone());
			this.lblEmailContato.setText(Contato.getEmail());
		} else {
			this.lblNomeContato.setText("");
			this.lblTelefoneContato.setText("");
			this.lblEmailContato.setText("");
		}
	}

	public boolean onCloseQuery() {
		Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
		alerta.setTitle("Pergunta");
		alerta.setHeaderText("Deseja sair do cadastro de Contato?");
		ButtonType buttonTypeNO = ButtonType.NO;
		ButtonType buttonTypeYES = ButtonType.YES;
		alerta.getButtonTypes().setAll(buttonTypeYES, buttonTypeNO);
		Optional<ButtonType> result = alerta.showAndWait();
		return result.get() == buttonTypeYES ? true : false;
	}


	public boolean onShowTelaContatoEditar(Contato Contato, String operacao) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ifsc/tds/Vitor/Cassio/Igor/view/ContatoEdit.fxml"));
			Parent ContatoEditXML = loader.load();

			Stage janelaContatoEditar = new Stage();
			janelaContatoEditar.setTitle("Cadastro de Contato" + operacao);
			janelaContatoEditar.initModality(Modality.APPLICATION_MODAL);
			janelaContatoEditar.resizableProperty().setValue(Boolean.FALSE);

			Scene ContatoEditLayout = new Scene(ContatoEditXML);
			janelaContatoEditar.setScene(ContatoEditLayout);

			ContatoEditController ContatoEditController = loader.getController();
			ContatoEditController.setJanelaContatoEdit(janelaContatoEditar);
			ContatoEditController.populaTela(Contato);

			janelaContatoEditar.showAndWait();

			return ContatoEditController.isOkClick();
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	public List<Contato> retornaListagemContato() {
		if (this.getContatoDAO() == null) {
			this.setContatoDAO(new ContatoDAO());
		}
		return this.getContatoDAO().getAll();
	}			
}
