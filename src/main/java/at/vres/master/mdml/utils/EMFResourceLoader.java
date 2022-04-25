package at.vres.master.mdml.utils;

import MLModel.MLModelPackage;
import at.vres.asit.bommodel.bom.BomPackage;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.papyrus.sysml16.activities.ActivitiesPackage;
import org.eclipse.papyrus.sysml16.allocations.AllocationsPackage;
import org.eclipse.papyrus.sysml16.blocks.BlocksPackage;
import org.eclipse.papyrus.sysml16.constraintblocks.ConstraintBlocksPackage;
import org.eclipse.papyrus.sysml16.deprecatedelements.DeprecatedElementsPackage;
import org.eclipse.papyrus.sysml16.modelelements.ModelElementsPackage;
import org.eclipse.papyrus.sysml16.portsandflows.PortsAndFlowsPackage;
import org.eclipse.papyrus.sysml16.requirements.RequirementsPackage;
import org.eclipse.papyrus.sysml16.sysml.SysMLPackage;
import org.eclipse.papyrus.sysml16.util.SysMLResource;
import org.eclipse.uml2.uml.UMLPlugin;
import org.eclipse.uml2.uml.resources.util.UMLResourcesUtil;

import java.io.File;
import java.util.*;

/**
 * Class for loading Resources with EMF when running transformations standalone (so without also launching an Eclipse instance)
 *
 * @author Matthias Rupp
 */
public class EMFResourceLoader {
    private static final String SYSML_PLUGIN_ID = "org.eclipse.papyrus.sysml16";

    /**
     * Initializes the resources for UML, using the provided Util class.
     *
     * @return The resource set with the initialized resources (not necessary currently)
     */
    private static ResourceSet initUMLResources() {
        ResourceSet res = new ResourceSetImpl();
        UMLResourcesUtil.init(res);
        return res;
    }

    /**
     * Initializes the resources needed for Papyrus SysML 1.6.
     * WARNING: always use the initUMLResources method first, which does important basic setup.
     */
    private static void initSysMLResources() {
        // Set some needed variables
        Map<String, String> segPaths = new HashMap<>();
        segPaths.put("resources/library", SysMLResource.LIBRARIES_PATHMAP);
        segPaths.put("resources/profile", SysMLResource.PROFILES_PATHMAP);
        String separator = "/";
        String jarPath = "libs\\org.eclipse.papyrus.sysml16_2.0.0.202106100115.jar";

        // Register Packages and initialize NS URI to profile location map
        loadResourceFromParameters(SysMLPackage.eNS_URI, SysMLPackage.eINSTANCE, SysMLResource.PROFILE_PATH, "SysML");
        loadResourceFromParameters(BlocksPackage.eNS_URI, BlocksPackage.eINSTANCE, SysMLResource.PROFILE_PATH,
                "SysML.package_packagedElement_Blocks");
        loadResourceFromParameters(RequirementsPackage.eNS_URI, RequirementsPackage.eINSTANCE,
                SysMLResource.PROFILE_PATH, "SysML.package_packagedElement_Requirements");
        loadResourceFromParameters(ActivitiesPackage.eNS_URI, ActivitiesPackage.eINSTANCE, SysMLResource.PROFILE_PATH,
                "SysML.package_packagedElement_Activities");
        loadResourceFromParameters(AllocationsPackage.eNS_URI, AllocationsPackage.eINSTANCE, SysMLResource.PROFILE_PATH,
                "SysML.package_packagedElement_Allocations");
        loadResourceFromParameters(ConstraintBlocksPackage.eNS_URI, ConstraintBlocksPackage.eINSTANCE,
                SysMLResource.PROFILE_PATH, "SysML.package_packagedElement_ConstraintBlocks");
        loadResourceFromParameters(ModelElementsPackage.eNS_URI, ModelElementsPackage.eINSTANCE,
                SysMLResource.PROFILE_PATH, "SysML.package_packagedElement_ModelElements");
        loadResourceFromParameters(PortsAndFlowsPackage.eNS_URI, PortsAndFlowsPackage.eINSTANCE,
                SysMLResource.PROFILE_PATH, "SysML.package_packagedElement_Ports_u0026Flows");
        loadResourceFromParameters(DeprecatedElementsPackage.eNS_URI, DeprecatedElementsPackage.eINSTANCE,
                SysMLResource.PROFILE_PATH, "SysML.package_packagedElement_DeprecatedElements");

        // init the URI converter map
        initURIConverterMap(SysMLResource.PROFILE_PATH, jarPath, SYSML_PLUGIN_ID, segPaths, separator);

    }

    /**
     * Initializes the package registry for the BOM package (the other steps do not seem to be necessary for the BOM).
     */
    private static void initBOM() {
        EPackage.Registry.INSTANCE.put(BomPackage.eNS_URI, BomPackage.eINSTANCE);
    }

    /**
     * Runs the three basic resource initialization methods needed for most ASID transformations.
     */
    public static void initBaseResources() {
        initUMLResources();
        initSysMLResources();
        initBOM();
        initML();
    }

    public static void initML() {
        String xmiID = "_UIy-AGmUEeyUs7Tj71US2A";
        String profilePathmap = "pathmap://ML_METAMODEL/";
        String profileLocation = "pathmap://ML_METAMODEL/ML.profile.uml";
        String jarPath = "libs/at.vres.asid.master.metamodel.ml-0.8.0-SNAPSHOT.jar";
        String pluginId = "at.vres.asid.master.metamodel.ml";

        loadResourceFromParameters(MLModelPackage.eNS_URI, MLModelPackage.eINSTANCE, profileLocation, xmiID);

        Map<String, String> segsAndPaths = new HashMap<>();
        segsAndPaths.put("model", profilePathmap);
        initURIConverterMap(profilePathmap, jarPath, pluginId, segsAndPaths, "/");
    }

    /**
     * Initializes the package and the NsURI to profile location map with the given parameter.
     * Do not forget to also call initNsURIToProfileLocationMap after this.
     *
     * @param nsURI           The nsURI of the resource to load. Usually in a generated constant in the shape "ProfilePackage.eNS_URI".
     * @param packageInstance The instance of the resource package. Usually in a generated constant in the shape "ProfilePackage.eINSTANCE".
     * @param profileLocation The location to the profile using the pathmap. Usually in the form of "pathmap://SysML16_PROFILES/SysML.profile.uml".
     *                        Needs to point to the uml file of the profile. Can usually be found in the plugin.xml of the profile plugin.
     * @param xmiId           The xmiID of the resource to load. This can be found in the uml file (open with a text editor),
     *                        in the shape of <uml:Profile xmi:id="__m4NgNflEeu5LPM6oXxvlQ" ...", where the string after xmi:id is what you want.
     */
    public static void loadResourceFromParameters(String nsURI, Object packageInstance, String profileLocation,
                                                  String xmiId) {
        initPackage(EPackage.Registry.INSTANCE, nsURI, packageInstance);
        initNsURIToProfileLocationMap(UMLPlugin.getEPackageNsURIToProfileLocationMap(), nsURI, profileLocation, xmiId);
    }

    /**
     * Adds package with given parameters to package registry
     *
     * @param packageRegistry The package registry to add to, usually EPackage.Registry.INSTANCE for us.
     * @param nsURI           The nsURI of the resource to load. Usually in a generated constant in the shape "ProfilePackage.eNS_URI".
     * @param packageInstance The instance of the resource package. Usually in a generated constant in the shape "ProfilePackage.eINSTANCE".
     */
    private static void initPackage(EPackage.Registry packageRegistry, String nsURI, Object packageInstance) {
        packageRegistry.put(nsURI, packageInstance);
    }

    /**
     * Adds the mapping between nsURI and profile location to the UMLPlugin.getEPackageNsURIToProfileLocationMap().
     *
     * @param uriMap          The uriMap to add to, usually UMLPlugin.getEPackageNsURIToProfileLocationMap()-
     * @param nsURI           The nsURI of the resource to load. Usually in a generated constant in the shape "ProfilePackage.eNS_URI".
     * @param profileLocation The location to the profile using the pathmap. Usually in the form of "pathmap://SysML16_PROFILES/SysML.profile.uml".
     * @param xmiID           The xmiID of the resource to load. This can be found in the uml file (open with a text editor),
     *                        in the shape of <uml:Profile xmi:id="__m4NgNflEeu5LPM6oXxvlQ" ...", where the string after xmi:id is what you want.
     */
    private static void initNsURIToProfileLocationMap(Map<String, URI> uriMap, String nsURI, String profileLocation,
                                                      String xmiID) {
        URI profileURI = URI.createURI(profileLocation + "#" + xmiID);
        uriMap.put(nsURI, profileURI);
    }

    /**
     * Helper method for splitting a string at the given separator.
     *
     * @param toSplit   The String you want to split.
     * @param separator The separator you want to split it at.
     * @return A string list containing the split segments.
     */
    private static List<String> splitStringWithDelimiter(String toSplit, String separator) {
        return Arrays.asList(toSplit.split(separator));
    }

    /**
     * Helper method for recursively appending segments to an URI.
     *
     * @param segmentsIterator The iterator for the list of segments to iterate over (use List.iterator() to get this).
     * @param toAppendTo       The URI to start appending to.
     * @return The URI after the final segment was appended.
     */
    private static URI segmentAppender(Iterator<String> segmentsIterator, URI toAppendTo) {
        if (segmentsIterator.hasNext()) {
            URI temp = toAppendTo.appendSegment(segmentsIterator.next());
            temp = segmentAppender(segmentsIterator, temp);
            toAppendTo = temp;
        }
        return toAppendTo;
    }

    /**
     * Initializes the URI converter map based on the given parameters.
     *
     * @param profilePathmap      The pathmap of the profile. Usually in the form of "pathmap://SysML16_PROFILES/".
     * @param jarPath             The absolute path to the jar file that was built for the profile plugin.
     * @param pluginID            The ID of the plugin (can be found in the MANIFEST.MF file, usually the same as the name of the plugin project, e.g. org.eclipse.papyrus.sysml16).
     * @param segmentsAndPathmaps A map containing the segment as the key and the pathmap to map to as the value. The segment should be relative from the root of the jar file given as parameter
     *                            and delimited with the separator given as parameter, e.g. "resources/library".
     * @param separator           The separator used in the segments passed.
     */
    public static void initURIConverterMap(String profilePathmap, String jarPath, String pluginID,
                                           Map<String, String> segmentsAndPathmaps, String separator) {
        Map<URI, URI> uriConverterMap = URIConverter.URI_MAP;
        File jarFile = new File(jarPath);
        URI jarURI = createJarURI(jarFile);
        segmentsAndPathmaps.forEach((segment, pathmap) -> {
            List<String> segmentsPath = splitStringWithDelimiter(segment, separator);
            URI tempURI = segmentAppender(segmentsPath.iterator(), jarURI);
            mapURIs(uriConverterMap, pathmap, tempURI, pluginID);
        });
    }

    /**
     * Helper method for generating a URI for a jar file based on the file.
     *
     * @param jarFile The jar file the URI is to be created for.
     * @return The created jar-file-URI.
     */
    private static URI createJarURI(File jarFile) {
        return URI.createURI("jar:file:/" + jarFile.getAbsolutePath() + "!/");
    }

    /**
     * Helper Method blatantly stolen from UMLResources.Util. Unfortunately not documented there, so take this with a grain of salt.
     *
     * @param uriMap   The URI map to put the mapping into.
     * @param uri      The URI to map as a String
     * @param location The location to map as an URI
     * @param pluginID he ID of the plugin (can be found in the MANIFEST.MF file, usually the same as the name of the plugin project, e.g. org.eclipse.papyrus.sysml16).
     */
    private static void mapURIs(Map<URI, URI> uriMap, String uri, URI location, String pluginID) {

        URI prefix = URI.createURI(uri);

        // ensure trailing separator (make it a "URI prefix")
        if (!prefix.hasTrailingPathSeparator()) {
            prefix = prefix.appendSegment(""); //$NON-NLS-1$
        }

        // same with the location
        if (!location.hasTrailingPathSeparator()) {
            location = location.appendSegment(""); //$NON-NLS-1$
        }

        uriMap.put(prefix, location);

        // and platform URIs, too
        String folder = location.segment(location.segmentCount() - 2);
        String platformURI = String.format("%s/%s/", //$NON-NLS-1$
                pluginID, folder);
        System.out.println("PLATFORM URI TO MAP: " + platformURI);
        uriMap.put(URI.createPlatformPluginURI(platformURI, true), location);
        uriMap.put(URI.createPlatformResourceURI(platformURI, true), location);
    }

    /**
     * Method for loading a model with a given path into a ResourceSet for further manipulation
     *
     * @param modelPath The path to the model to load
     * @return A new ResourceSet containing the loaded model
     */
    public static ResourceSet loadModel(String modelPath) {
        URI uri1 = URI.createFileURI(modelPath);
        Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("ecore", new EcoreResourceFactoryImpl());
        ResourceSet resourceSet1 = new ResourceSetImpl();
        resourceSet1.setResourceFactoryRegistry(Resource.Factory.Registry.INSTANCE);
        resourceSet1.getResource(uri1, true);
        return resourceSet1;
    }

    /**
     * Method for loading UML, SysML and BOM metamodels and then loading a model with the given path into a new ResourceSet
     *
     * @param modelPath The path to the model to load
     * @return A new ResourceSet containing the loaded model
     */
    public static ResourceSet fullLoadWithModel(String modelPath) {
        initBaseResources();
        return loadModel(modelPath);
    }

    /**
     * Method for loading a model into an existing ResourceSet
     *
     * @param modelPath The path to the model to load
     * @param resSet    The ResourceSet to load the model into
     * @return The ResourceSet passed as parameter with the additional model loaded into it
     */
    public static ResourceSet loadModelIntoExistingResourceSet(String modelPath, ResourceSet resSet) {
        URI uri1 = URI.createFileURI(modelPath);
        resSet.setResourceFactoryRegistry(Resource.Factory.Registry.INSTANCE);
        resSet.getResource(uri1, true);
        return resSet;
    }
}
