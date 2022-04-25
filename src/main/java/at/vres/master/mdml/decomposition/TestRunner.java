package at.vres.master.mdml.decomposition;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import MLModel.ML;
import at.vres.master.mdml.tbcg.VelocityTest;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.uml2.types.TypesFactory;
import org.eclipse.uml2.types.TypesPackage;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Pseudostate;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.Vertex;
import org.eclipse.uml2.uml.resource.UML212UMLExtendedMetaData;
import org.eclipse.uml2.uml.resource.UMLResource;

import MLModel.Attributes.ML_Attribute_Input;
import MLModel.DataStorage.CSV;

public class TestRunner {
	private static final Map<String, MLInformationHolder> ml = new LinkedHashMap<>();
	private static final List<MLInformationHolder> orderedML = new LinkedList<>();

	private static HashMap<String, Object> addStereotypeAttributesToHashMap(HashMap<String, Object> attMap,
			ML connectedBlock) {
		Class base_Class = connectedBlock.getBase_Class();
		if (base_Class != null) {
			MLInformationHolder mlih = new MLInformationHolder(base_Class.getQualifiedName(), base_Class.getName());
			// System.out.println("BASE CLASS: " + base_Class);
			base_Class.getAppliedStereotypes().forEach(s -> {
				if (!(s.getName().equals("Block"))) {
					final Map<String, Object> attributes = new HashMap<>();
					s.getAllAttributes().forEach(a -> {
						if (!(a.getName().equals("base_Class"))) {
							String key = base_Class.getName() + "&&" + s.getName() + "&&" + a.getName();
							Object value = base_Class.getValue(s, a.getName());
							if (value instanceof ML_Attribute_Input) {
								attributes.put(a.getName(),
										((ML_Attribute_Input) value).getBase_Property().getQualifiedName());
							} else if (value instanceof List<?>) {
								List<?> vl = (List<?>) value;
								if (!vl.isEmpty()) {
									if (vl.get(0) instanceof ML_Attribute_Input) {
										List<String> quali = new LinkedList<>();
										vl.forEach(mla -> {
											ML_Attribute_Input attIn = (ML_Attribute_Input) mla;
											quali.add(attIn.getBase_Property().getQualifiedName());
										});
										attributes.put(a.getName(), quali);
									} else {
										attributes.put(a.getName(), value);
									}
								} else {
									attributes.put(a.getName(), value);
								}
							} else {
								attributes.put(a.getName(), value);
							}

							attMap.put(key, value);
						}
						// System.out.println("TEST VALUE " + a.getName() + ": " + value);
					});
					mlih.getStereotypes().put(s.getName(), attributes);
				}
			});
			base_Class.getOwnedAttributes().forEach(p -> {
				String key = base_Class.getName() + "&&" + p.getName();
				System.out.println("PROP TYPE: " + p.getType());
				if (p.getType() instanceof Class) {
					mlih.getProperties().put(p.getName(), p.getType().getQualifiedName());
				} else {
					mlih.getProperties().put(p.getName(), p.getDefault());
				}
				attMap.put(key, p.getDefault());
			});

			base_Class.getAssociations().forEach(assoc -> {
				// System.out.println("ASSOCIATION: " + assoc);
				// System.out.println("OWNER ASSOCIATION: " + assoc.getOwner());
				assoc.getMembers().forEach(mem -> {
					Element owner = mem.getOwner();
					// System.out.println("MEMBER END: " + mem.getName());
					// System.out.println("OWNER OF MEMBER END: " + owner);
					if (owner != base_Class) {
						if (owner instanceof Class) {
							mlih.getParts().put(mem.getName(), ((Class) owner).getQualifiedName());
						}
						// String key = base_Class.getName() + "&&MERGE";
						// attMap.put(key, owner);
					}
				});
			});
			ml.putIfAbsent(base_Class.getQualifiedName(), mlih);
		} else {
			System.out.println("BASE CLASS IS NULL FOR: " + connectedBlock);
			if (connectedBlock instanceof ML_Attribute_Input) {
				ML_Attribute_Input input = (ML_Attribute_Input) connectedBlock;
				Property base_Property = input.getBase_Property();
				MLInformationHolder mlih = new MLInformationHolder(base_Property.getQualifiedName(),
						base_Property.getName());
				base_Property.getAppliedStereotypes().forEach(ps -> {
					if (!(ps.getName().equals("Block"))) {
						final Map<String, Object> attributes = new HashMap<>();
						ps.getAllAttributes().forEach(a -> {
							if (!(a.getName().equals("base_Property"))) {
								String key = base_Property.getName() + "&&" + ps.getName() + "&&" + a.getName();
								Object value = base_Property.getValue(ps, a.getName());

								attributes.put(a.getName(), value);

								attMap.put(key, value);
							}
							// System.out.println("TEST VALUE " + a.getName() + ": " + value);
						});
						mlih.getStereotypes().put(ps.getName(), attributes);
					}
				});
				mlih.getProperties().putIfAbsent(base_Property.getName(), base_Property.getDefault());
				Association association = base_Property.getAssociation();
				if (association != null) {
					association.getMemberEnds().forEach(memEnd -> {
						Element owner = memEnd.getOwner();
						// System.out.println("MEMBER END: " + mem.getName());
						// System.out.println("OWNER OF MEMBER END: " + owner);
						if (owner != base_Property) {
							if (owner instanceof Class) {
								mlih.getParts().put(memEnd.getName(), ((Class) owner).getQualifiedName());
							}
							// String key = base_Class.getName() + "&&MERGE";
							// attMap.put(key, owner);
						}
					});
				}
				ml.putIfAbsent(base_Property.getQualifiedName(), mlih);
			}
		}
		return attMap;
	}

	public static void doExtraction() {
		TestModelExtractor ex = new TestModelExtractor();
		ResourceSet res = ex.defaultLoad();
		res.getAllContents().forEachRemaining(con -> {
			if (con instanceof ML) {
				addStereotypeAttributesToHashMap(new HashMap<>(), (ML) con);
			}
		});

		final HashMap<String, Object> attMap = new HashMap<>();
		res.getAllContents().forEachRemaining(con -> {
			if (con instanceof Pseudostate) {
				Pseudostate ps = (Pseudostate) con;
				// System.out.println("PSEUDOSTATE: " + ps);
				ps.getOutgoings().forEach(out -> {
					Vertex target = out.getTarget();

					HashMap<String, Object> followVertex = followVertex(target, 0, attMap);
					// followVertex.forEach((key, value) -> System.out.println("{ Key " + key + ": "
					// + value + " }"));
				});
			}
		});
		prettyPrintMLInformationHolderList(orderedML);
		prettyPrintMLInformationHolderMap(ml);
	}

	public static void main(String[] args) {
		//doExtraction();
		VelocityTest.velTestRun("test.vm");
	}


	private static void prettyPrintMLInformationHolderMap(Map<String, MLInformationHolder> mapToPrint) {
		mapToPrint.forEach((key, value) -> {
			System.out.println(
					"------------------------------------------------------------------------------------------------------------------------------------");
			System.out.println("{ ");
			System.out.println("\t" + key);
			System.out.println("\t\tName: " + value.getName());
			System.out.println("\t\tStereotypes: ");
			value.getStereotypes().forEach((skey, svalue) -> {
				System.out.println("\t\t\t" + skey + ": ");
				svalue.forEach((akey, avalue) -> {
					System.out.println("\t\t\t\t" + akey + ": " + avalue);
				});
			});
			System.out.println("\t\tProperties: ");
			value.getProperties().forEach((pkey, pvalue) -> {
				System.out.println("\t\t\t" + pkey + ": " + pvalue);
			});
			System.out.println("\t\tParts: ");
			value.getParts().forEach((partKey, partValue) -> {
				System.out.println("\t\t\t" + partKey + ": " + partValue);
			});
			System.out.println("}");
			System.out.println(
					"------------------------------------------------------------------------------------------------------------------------------------");
		});
	}

	private static void prettyPrintMLInformationHolderList(List<MLInformationHolder> listToPrint) {
		listToPrint.forEach(value -> {
			System.out.println(
					"------------------------------------------------------------------------------------------------------------------------------------");
			System.out.println("{ ");
			System.out.println("\tQualifiedName: " + value.getQualifiedName());
			System.out.println("\tName: " + value.getName());
			System.out.println("\tStereotypes: ");
			value.getStereotypes().forEach((skey, svalue) -> {
				System.out.println("\t\t" + skey + ": ");
				svalue.forEach((akey, avalue) -> {
					System.out.println("\t\t\t" + akey + ": " + avalue);
				});
			});
			System.out.println("\tProperties: ");
			value.getProperties().forEach((pkey, pvalue) -> {
				System.out.println("\t\t" + pkey + ": " + pvalue);
			});
			System.out.println("\tParts: ");
			value.getParts().forEach((partKey, partValue) -> {
				System.out.println("\t\t" + partKey + ": " + partValue);
			});
			System.out.println("}");
			System.out.println(
					"------------------------------------------------------------------------------------------------------------------------------------");
		});
	}

	public static HashMap<String, Object> followVertex(Vertex state, int depth, HashMap<String, Object> attMap) {
		// System.out.println("VERTEX: " + state + ", depth: " + depth);
		state.getAppliedStereotypes().forEach(st -> {
			// System.out.println(st.getName());
			Object value = state.getValue(st, "ML_Block");
			if (value instanceof ML) {
				String mlKey = ((ML) value).getBase_Class().getQualifiedName();
				if (ml.containsKey(mlKey)) {
					orderedML.add(ml.get(mlKey));
				} else {
					System.out.println("UNKNOWN ML ELEMENT!!!");
				}
			}
		});
		state.getOutgoings().forEach(out -> followVertex(out.getTarget(), depth + 1, attMap));
		return attMap;
	}
}
