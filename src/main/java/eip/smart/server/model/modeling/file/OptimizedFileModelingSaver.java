package eip.smart.server.model.modeling.file;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import eip.smart.cscommons.model.JSONViews;
import eip.smart.cscommons.model.geometry.PointCloud3D;
import eip.smart.cscommons.model.modeling.Modeling;
import eip.smart.server.model.modeling.ModelingLogic;
import eip.smart.server.util.exception.ModelingNotFoundException;
import eip.smart.server.util.exception.ModelingObsoleteException;

import java.io.File;
import java.io.IOException;

/**
 * This class implements the standard way of storing a Modeling to a file, through Java serialization.
 */
public class OptimizedFileModelingSaver extends FileModelingSaver {

	private final ObjectMapper mapper = new ObjectMapper();

	public OptimizedFileModelingSaver() {
		this.mapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false);
		this.mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
	}

	@Override
	public Modeling load(String name) throws ModelingNotFoundException, ModelingObsoleteException {
		FileModelingSaver.LOGGER.info("Loading modelisation {}", name);
		ModelingLogic m = new ModelingLogic(this.preview(name));
		try {
			PointCloud3D pcl3d = loadJSONObject(getMappingFile(name), PointCloud3D.class);
			m.addPoints(pcl3d.getPoints());
		} catch (IOException e) {
			throw new ModelingObsoleteException(e);
		}

		return (m);
	}

	private <T> void saveJSONObject(File file, T o) throws IOException {
		mapper.writerWithView(JSONViews.DISK.class).writeValue(file, o);
	}

	private <T> T loadJSONObject(File file, Class<T> type) throws IOException {
		T o = mapper.readerWithView(JSONViews.DISK.class).forType(type).readValue(file);
		return (o);
	}

	@Override
	public void save(Modeling modeling) {
		File fileModeling = getModelingFile(modeling.getName());
		File fileMapping = getMappingFile(modeling.getName());

		try {
			this.saveJSONObject(fileModeling, modeling);
			this.saveJSONObject(fileMapping, modeling.getMapping());
		} catch (IOException e) {
			FileModelingSaver.LOGGER.error("Error saving the modeling", e);
			return;
		}
		FileModelingSaver.LOGGER.info("Successfully saved modelisation at {} and {}", fileModeling.getAbsolutePath(), fileMapping.getAbsolutePath());

	}

	@Override
	public Modeling preview(String name) throws ModelingNotFoundException, ModelingObsoleteException {
		Modeling modeling = null;
		File file = getModelingFile(name);
		//FileModelingSaver.LOGGER.info("Loading modelisation at " + file.getAbsolutePath());
		try {
			modeling = this.loadJSONObject(file, Modeling.class);
		} catch (IOException e) {
			throw new ModelingObsoleteException(e);
		}
		return (modeling);
	}
}
