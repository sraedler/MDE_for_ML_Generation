package at.vres.master.mdml.decomposition;

import at.vres.master.mdml.utils.EMFResourceLoader;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;

public class TestModelExtractor {
	private static final String RELATIVE_TEST_MODEL = "src/main/resources/UC1_Weather/UC1_Weather.uml";

	public ResourceSet defaultLoad() {
		return load(RELATIVE_TEST_MODEL);
	}

	public static ResourceSet load(String modelToLoad) {
		EMFResourceLoader.initBaseResources();
		URI uri1 = URI.createFileURI(modelToLoad);
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("ecore", new EcoreResourceFactoryImpl());
		ResourceSet resourceSet1 = new ResourceSetImpl();
		resourceSet1.setResourceFactoryRegistry(Resource.Factory.Registry.INSTANCE);
		resourceSet1.getResource(uri1, true);

		return resourceSet1;
	}
}
