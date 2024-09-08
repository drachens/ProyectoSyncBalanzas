package com.marsol.sync.service.transform;


import com.marsol.sync.model.Scale;

/**
 * Esta inteface proporciona un contrato para clases que implementen
 * la transformación de los datos al formato de la balanza: PLU y Notas.
 * 
 * Cualquier clase que implemente esta interfaz deberá proporcionar su propia implementación
 * de los métodos.
 */

public interface TransformationStrategy<T> {
	/**
	 * Transforma los datos de productos a formato PLU
	 */
	void transformDataPLUsAsistida(Scale scale);
	/**
	 * Tansforma los textos a formato Notas
	 */
	void transformDataNotes(Scale scale);


	/**
	 * Transforma la ruta de las imágenes. Aun no implementado
	 */
	void transformDataPLUsAutoservicio(Scale scale);

}
