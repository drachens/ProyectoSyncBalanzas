package com.marsol.sync.model;

/*
	Esta clase establece un modelo para la creación de objetos tipo Infonut,
	los cuales respetan la estructura de los Objetos en el JSON retornado
	por WS cuando se consulta la Información Nutricional.
 */

public class Infonut {

	private int id;
	private int store_nbr;
	private int dept_nbr;
	private int item_nbr;
	private int plu_nbr;
	private String item_status_code;
	private boolean es_etiqueta_propia;
	private boolean es_texto_Alternativo;
	private String texto_alternativo;
	private String procedencia;
	private String alergernos;
	private String resolucion;
	private String tablaNutricional;
	private String porcion;
	private String porcionesxEnvase;
	private String subtituloTablaNut;
	private String textoEnergiaContigencia;
	private String muestraEnergia;
	private String textoEnergia;
	private String muestraProteinas;
	private String textoProteinas;
	private String muestraGrasaTotal;
	private String textoGrasaTotal;
	private String muestraGrasaSat;
	private String textoGrasaSat;
	private String muestraGPoliin;
	private String textoGPoliin;
	private String muestraAcGrasosTrans;
	private String textoAcGrasosTrans;
	private String muestraColesterol;
	private String textoColesterol;
	private String muestraHdeCdisp;
	private String textoHdeCdisp;
	private String muestraAzucaresTot;
	private String textoAzucaresTot;
	private String muestraSodio;
	private String textoSodio;
	private int taraNutricional;
	private int taraPorcentual;
	private int diasPerecibilidad;
	private String condicionMantencion;
	private String ingredientes;
	private boolean refresh;
	private String textoRefresh;
	private boolean productoCongelado;
	private String textoCongelado;
	private boolean importado;
	private String textoImportado;
	private boolean llevaInfoLocal;
	private String imagenSellos;
	
	public Infonut() {

	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getStore_nbr() {
		return store_nbr;
	}
	public void setStore_nbr(int store_nbr) {
		this.store_nbr = store_nbr;
	}
	public int getDept_nbr() {
		return dept_nbr;
	}
	public void setDept_nbr(int dept_nbr) {
		this.dept_nbr = dept_nbr;
	}
	public int getItem_nbr() {
		return item_nbr;
	}
	public void setItem_nbr(int item_nbr) {
		this.item_nbr = item_nbr;
	}
	public int getPlu_nbr() {
		return plu_nbr;
	}
	public void setPlu_nbr(int plu_nbr) {
		this.plu_nbr = plu_nbr;
	}
	public String getItem_status_code() {
		return item_status_code;
	}
	public void setItem_status_code(String item_status_code) {
		this.item_status_code = item_status_code;
	}
	public boolean isEs_etiqueta_propia() {
		return es_etiqueta_propia;
	}
	public void setEs_etiqueta_propia(boolean es_etiqueta_propia) {
		this.es_etiqueta_propia = es_etiqueta_propia;
	}
	public boolean isEs_texto_Alternativo() {
		return es_texto_Alternativo;
	}
	public void setEs_texto_Alternativo(boolean es_texto_Alternativo) {
		this.es_texto_Alternativo = es_texto_Alternativo;
	}
	public String getTexto_alternativo() {
		return texto_alternativo;
	}
	public void setTexto_alternativo(String texto_alternativo) {
		this.texto_alternativo = texto_alternativo;
	}
	public String getProcedencia() {
		return procedencia;
	}
	public void setProcedencia(String procedencia) {
		this.procedencia = procedencia;
	}
	public String getAlergernos() {
		return alergernos;
	}
	public void setAlergernos(String alergernos) {
		this.alergernos = alergernos;
	}
	public String getResolucion() {
		resolucion = resolucion.replace("|","{$0A}");
		return resolucion;
	}
	public void setResolucion(String resolucion) {
		this.resolucion = resolucion;
	}
	public String getTablaNutricional() {
		return tablaNutricional;
	}
	public void setTablaNutricional(String tablaNutricional) {
		this.tablaNutricional = tablaNutricional;
	}
	public String getPorcion() {
		return porcion;
	}
	public void setPorcion(String porcion) {
		this.porcion = porcion;
	}
	public String getPorcionesxEnvase() {
		return porcionesxEnvase;
	}
	public void setPorcionesxEnvase(String porcionesxEnvase) {
		this.porcionesxEnvase = porcionesxEnvase;
	}
	public String getSubtituloTablaNut() {
		return subtituloTablaNut;
	}
	public void setSubtituloTablaNut(String subtituloTablaNut) {
		this.subtituloTablaNut = subtituloTablaNut;
	}
	public String getTextoEnergiaContigencia() {
		return textoEnergiaContigencia;
	}
	public void setTextoEnergiaContigencia(String textoEnergiaContigencia) {
		this.textoEnergiaContigencia = textoEnergiaContigencia;
	}
	public String getMuestraEnergia() {
		return muestraEnergia;
	}
	public void setMuestraEnergia(String muestraEnergia) {
		this.muestraEnergia = muestraEnergia;
	}
	public String getTextoEnergia() {
		return textoEnergia;
	}
	public void setTextoEnergia(String textoEnergia) {
		this.textoEnergia = textoEnergia;
	}
	public String getMuestraProteinas() {
		return muestraProteinas;
	}
	public void setMuestraProteinas(String muestraProteinas) {
		this.muestraProteinas = muestraProteinas;
	}
	public String getTextoProteinas() {
		return textoProteinas;
	}
	public void setTextoProteinas(String textoProteinas) {
		this.textoProteinas = textoProteinas;
	}
	public String getMuestraGrasaTotal() {
		return muestraGrasaTotal;
	}
	public void setMuestraGrasaTotal(String muestraGrasaTotal) {
		this.muestraGrasaTotal = muestraGrasaTotal;
	}
	public String getTextoGrasaTotal() {
		return textoGrasaTotal;
	}
	public void setTextoGrasaTotal(String textoGrasaTotal) {
		this.textoGrasaTotal = textoGrasaTotal;
	}
	public String getMuestraGrasaSat() {
		return muestraGrasaSat;
	}
	public void setMuestraGrasaSat(String muestraGrasaSat) {
		this.muestraGrasaSat = muestraGrasaSat;
	}
	public String getTextoGrasaSat() {
		return textoGrasaSat;
	}
	public void setTextoGrasaSat(String textoGrasaSat) {
		this.textoGrasaSat = textoGrasaSat;
	}
	public String getMuestraGPoliin() {
		return muestraGPoliin;
	}
	public void setMuestraGPoliin(String muestraGPoliin) {
		this.muestraGPoliin = muestraGPoliin;
	}
	public String getTextoGPoliin() {
		return textoGPoliin;
	}
	public void setTextoGPoliin(String textoGPoliin) {
		this.textoGPoliin = textoGPoliin;
	}
	public String getMuestraAcGrasosTrans() {
		return muestraAcGrasosTrans;
	}
	public void setMuestraAcGrasosTrans(String muestraAcGrasosTrans) {
		this.muestraAcGrasosTrans = muestraAcGrasosTrans;
	}
	public String getTextoAcGrasosTrans() {
		return textoAcGrasosTrans;
	}
	public void setTextoAcGrasosTrans(String textoAcGrasosTrans) {
		this.textoAcGrasosTrans = textoAcGrasosTrans;
	}
	public String getMuestraColesterol() {
		return muestraColesterol;
	}
	public void setMuestraColesterol(String muestraColesterol) {
		this.muestraColesterol = muestraColesterol;
	}
	public String getTextoColesterol() {
		return textoColesterol;
	}
	public void setTextoColesterol(String textoColesterol) {
		this.textoColesterol = textoColesterol;
	}
	public String getMuestraHdeCdisp() {
		return muestraHdeCdisp;
	}
	public void setMuestraHdeCdisp(String muestraHdeCdisp) {
		this.muestraHdeCdisp = muestraHdeCdisp;
	}
	public String getTextoHdeCdisp() {
		return textoHdeCdisp;
	}
	public void setTextoHdeCdisp(String textoHdeCdisp) {
		this.textoHdeCdisp = textoHdeCdisp;
	}
	public String getMuestraAzucaresTot() {
		return muestraAzucaresTot;
	}
	public void setMuestraAzucaresTot(String muestraAzucaresTot) {
		this.muestraAzucaresTot = muestraAzucaresTot;
	}
	public String getTextoAzucaresTot() {
		return textoAzucaresTot;
	}
	public void setTextoAzucaresTot(String textoAzucaresTot) {
		this.textoAzucaresTot = textoAzucaresTot;
	}
	public String getMuestraSodio() {
		return muestraSodio;
	}
	public void setMuestraSodio(String muestraSodio) {
		this.muestraSodio = muestraSodio;
	}
	public String getTextoSodio() {
		return textoSodio;
	}
	public void setTextoSodio(String textoSodio) {
		this.textoSodio = textoSodio;
	}
	public int getTaraNutricional() {
		return taraNutricional;
	}
	public void setTaraNutricional(int taraNutricional) {
		this.taraNutricional = taraNutricional;
	}
	public int getTaraPorcentual() {
		return taraPorcentual;
	}
	public void setTaraPorcentual(int taraPorcentual) {
		this.taraPorcentual = taraPorcentual;
	}
	public int getDiasPerecibilidad() {
		return diasPerecibilidad;
	}
	public void setDiasPerecibilidad(int diasPerecibilidad) {
		this.diasPerecibilidad = diasPerecibilidad;
	}
	public String getCondicionMantencion() {
		return condicionMantencion;
	}
	public void setCondicionMantencion(String condicionMantencion) {
		this.condicionMantencion = condicionMantencion;
	}
	public String getIngredientes() {
		return ingredientes;
	}
	public void setIngredientes(String ingredientes) {
		this.ingredientes = ingredientes;
	}
	public boolean isRefresh() {
		return refresh;
	}
	public void setRefresh(boolean refresh) {
		this.refresh = refresh;
	}
	public String getTextoRefresh() {
		return textoRefresh;
	}
	public void setTextoRefresh(String textoRefresh) {
		this.textoRefresh = textoRefresh;
	}
	public boolean isProductoCongelado() {
		return productoCongelado;
	}
	public void setProductoCongelado(boolean productoCongelado) {
		this.productoCongelado = productoCongelado;
	}
	public String getTextoCongelado() {
		return textoCongelado;
	}
	public void setTextoCongelado(String textoCongelado) {
		this.textoCongelado = textoCongelado;
	}
	public boolean isImportado() {
		return importado;
	}
	public void setImportado(boolean importado) {
		this.importado = importado;
	}
	public String getTextoImportado() {
		return textoImportado;
	}
	public void setTextoImportado(String textoImportado) {
		this.textoImportado = textoImportado;
	}
	public boolean isLlevaInfoLocal() {
		return llevaInfoLocal;
	}
	public void setLlevaInfoLocal(boolean llevaInfoLocal) {
		this.llevaInfoLocal = llevaInfoLocal;
	}
	public String getImagenSellos() {
		return imagenSellos;
	}
	public void setImagenSellos(String imagenSellos) {
		this.imagenSellos = imagenSellos;
	}
}
