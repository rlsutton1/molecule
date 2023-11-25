package au.com.sort;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import au.com.sort.fileloader.FileLoader;
import au.com.sort.fileloader.FileLoaderFactory;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

public class MoleculeInspectorApp extends Application
{

	private static final int SEPARATION = 18;
	private static List<Atom> primary;

	final XForm world = new XForm();

	final PerspectiveCamera camera = new PerspectiveCamera(true);
	final XForm cameraXForm = new XForm();
	final XForm cameraXForm2 = new XForm();
	final XForm cameraXForm3 = new XForm();
	final double cameraDistance = 400;

	final XForm moleculeGroupPrimary = new XForm();

	// final Group axisGroup = new Group();
	private PhongMaterial whiteMaterial;

	List<Sphere> primaryAtoms = new LinkedList<>();

	private void buildScene(Group root)
	{
		System.out.println("buildScene");
		root.getChildren().add(this.world);
	}

	@Override
	public void start(Stage primaryStage)
	{
		System.out.println("start");

		initColours();
		final Group root = new Group();
		buildScene(root);

		buildCamera(root);
		// buildAxes();
		buildMolecule();

		// SubScene subScene = new SubScene(subroot, 100, 200);

		Scene scene = new Scene(root, 1024, 768, true);
		scene.setFill(Color.GREY);

		primaryStage.setTitle("Molecule Sample Application");
		primaryStage.setScene(scene);
		primaryStage.show();

		scene.setCamera(this.camera);

		// root.getChildren().add(buildSubScene());
		// root.getChildren().add(test());

		handleMouse(scene);

	}

	BorderPane test()
	{
		Button button = new Button("Brute");
		BorderPane borderPane = new BorderPane();
		borderPane
				.setStyle("-fx-border-color: black;-fx-background-color: #66CCFF;");
		borderPane.setTop(button);

		borderPane.setMaxSize(2, 2);

		return borderPane;
	}

	SubScene buildSubScene()
	{

		Camera camera = new PerspectiveCamera();

		// -------------------
		// cameraXRotate = new Rotate(-5,Rotate.X_AXIS);
		// cameraYRotate = new Rotate(-50,Rotate.Y_AXIS);
		Translate cameraPosition2 = new Translate(0, 10, 10);
		//
		// camera.getTransforms().add(cameraXRotate);
		// camera.getTransforms().add(cameraYRotate);
		// camera.getTransforms().add(cameraPosition2);

		HBox hbox1 = new HBox();
		Button button = new Button("Brute");
		hbox1.getChildren().add(button);

		AnchorPane subroot = new AnchorPane();

		AnchorPane.setRightAnchor(hbox1, 0.0);
		AnchorPane.setBottomAnchor(hbox1, 0.0);

		subroot.getChildren().add(hbox1);

		SubScene subScene = new SubScene(subroot, 100, 200);
		subScene.setCamera(camera);

		// subScene.setScaleX(0.2);
		// subScene.setScaleY(0.2);
		// subScene.setScaleZ(0.2);

		return subScene;
	}

	/**
	 * The main() method is ignored in correctly deployed JavaFX application.
	 * main() serves only as fallback in case the application can not be
	 * launched through deployment artifacts, e.g., in IDEs with limited FX
	 * support. NetBeans ignores main().
	 * 
	 * @param args
	 *            the command line arguments
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws FileNotFoundException,
			IOException, InterruptedException
	{
		System.setProperty("prism.dirtyopts", "false");
		String stripList = null;
		if (args.length > 4)
		{
			stripList = args[3];
		}
		String atomFilter = null;
		if (args.length > 2)
		{
			atomFilter = args[2];
		}

		FileLoader loader = FileLoaderFactory.getFileLoader(args[0], args[1], atomFilter,
				stripList);

		primary = loader.getPrimary();

		// secondary = RelationshipSorter.getSorted(primary, secondary);
		launch(args);
	}

	private void buildCamera(Group root)
	{
		root.getChildren().add(this.cameraXForm);
		this.cameraXForm.getChildren().add(this.cameraXForm2);
		this.cameraXForm2.getChildren().add(this.cameraXForm3);
		this.cameraXForm3.getChildren().add(this.camera);
		this.cameraXForm3.setRotateZ(180.0);

		this.camera.setFieldOfView(7);
		this.camera.setNearClip(0.1);
		this.camera.setFarClip(10000.0);
		this.camera.setTranslateZ(-this.cameraDistance);
		this.cameraXForm.ry.setAngle(180);
		this.cameraXForm.rx.setAngle(180);
	}

	// private void buildAxes()
	// {
	// System.out.println("buildAxes()");
	// final PhongMaterial redMaterial = new PhongMaterial();
	// redMaterial.setDiffuseColor(Color.DARKRED);
	// redMaterial.setSpecularColor(Color.RED);
	//
	// final PhongMaterial greenMaterial = new PhongMaterial();
	// greenMaterial.setDiffuseColor(Color.DARKGREEN);
	// greenMaterial.setSpecularColor(Color.GREEN);
	//
	// final PhongMaterial blueMaterial = new PhongMaterial();
	// blueMaterial.setDiffuseColor(Color.DARKBLUE);
	// blueMaterial.setSpecularColor(Color.BLUE);
	//
	// final Box xAxis = new Box(240.0, 1, 1);
	// final Box yAxis = new Box(1, 240.0, 1);
	// final Box zAxis = new Box(1, 1, 240.0);
	//
	// xAxis.setMaterial(redMaterial);
	// yAxis.setMaterial(greenMaterial);
	// zAxis.setMaterial(blueMaterial);
	//
	// axisGroup.getChildren().addAll(xAxis, yAxis, zAxis);
	// world.getChildren().addAll(axisGroup);
	// }

	Map<String, PhongMaterial> atomToColourMap = new HashMap<>();
	List<PhongMaterial> availableColours = new LinkedList<>();

	void initColours()
	{
		this.atomToColourMap.put("N", new PhongMaterial(Color.BLUE));
		this.atomToColourMap.put("C", new PhongMaterial(Color.BLACK));
		this.atomToColourMap.put("O", new PhongMaterial(Color.RED));
		this.atomToColourMap.put("H", new PhongMaterial(Color.PINK));
		this.atomToColourMap.put("Al", new PhongMaterial(Color.GREY));

		this.availableColours.add(new PhongMaterial(Color.GREEN));
		this.availableColours.add(new PhongMaterial(Color.PURPLE));
		this.availableColours.add(new PhongMaterial(Color.YELLOW));
		this.availableColours.add(new PhongMaterial(Color.YELLOWGREEN));
		this.availableColours.add(new PhongMaterial(Color.BROWN));
		this.availableColours.add(new PhongMaterial(Color.ORANGE));
		this.availableColours.add(new PhongMaterial(Color.AQUAMARINE));
		this.availableColours.add(new PhongMaterial(Color.AZURE));
	}

	PhongMaterial getColor(String atom)
	{
		PhongMaterial color = this.atomToColourMap.get(atom);
		if (color == null)
		{
			color = this.availableColours.remove(0);
			this.atomToColourMap.put(atom, color);
			System.out.println("Adding colour for " + atom);
		}
		return color;
	}

	private void buildMolecule()
	{

		XForm moleculeXForm1 = new XForm();

		double min = Double.MAX_VALUE;
		double max = Double.MIN_VALUE;

		for (Atom atom : primary)
		{
			min = Math.min(min, atom.position.getX());
			min = Math.min(min, atom.position.getY());
			min = Math.min(min, atom.position.getZ());

			max = Math.max(max, atom.position.getX());
			max = Math.max(max, atom.position.getY());
			max = Math.max(max, atom.position.getZ());

		}
		double scalar = 20.0 / (max - min);

		for (Atom atom : primary)
		{
			XForm atomXForm = new XForm();

			Sphere sphere = new Sphere(1.0);
			sphere.setMaterial(getColor(atom.type));
			sphere.setTranslateX(atom.position.getX() * scalar);
			sphere.setTranslateY(atom.position.getY() * scalar);
			sphere.setTranslateZ(atom.position.getZ() * scalar);
			atomXForm.getChildren().add(sphere);
			if (atom.label != null && !atom.label.isEmpty())
			{
				String group = atom.label.split("-")[0];
				PhongMaterial color = getColor(group);
				sphere.setMaterial(color);
				//				Label label = new Label(atom.label);
				//				label.setTranslateX((atom.position.getX()) * scalar);
				//				label.setTranslateY(atom.position.getY() * scalar);
				//				label.setTranslateZ(atom.position.getZ() * scalar);
				//
				//				atomXForm.getChildren().add(label);
			}

			this.primaryAtoms.add(sphere);
			moleculeXForm1.getChildren().add(atomXForm);
			addPrimaryMouseHandler(sphere, atom);
		}

		this.moleculeGroupPrimary.setTranslateX(-SEPARATION);
		this.moleculeGroupPrimary.getChildren().add(moleculeXForm1);

		this.world.getChildren().addAll(this.moleculeGroupPrimary);
	}

	private double mousePosX;
	private double mousePosY;
	private double mouseOldX;
	private double mouseOldY;
	protected Integer lastSelected = null;

	Map<Atom, Atom> selected = new LinkedHashMap<>();

	void addPrimaryMouseHandler(final Sphere node, final Atom atom)
	{
		node.setOnMousePressed(new EventHandler<MouseEvent>()
		{

			@Override
			public void handle(MouseEvent me)
			{
				MoleculeInspectorApp.this.mousePosX = me.getSceneX();
				MoleculeInspectorApp.this.mousePosY = me.getSceneY();
				MoleculeInspectorApp.this.mouseOldX = me.getSceneX();
				MoleculeInspectorApp.this.mouseOldY = me.getSceneY();

				System.out.println(atom);

				if (me.getClickCount() == 1)
				{
					if (MoleculeInspectorApp.this.lastSelected != null)
					{
						PhongMaterial material = MoleculeInspectorApp.this.whiteMaterial;
						if (MoleculeInspectorApp.this.selected
								.get(primary.get(MoleculeInspectorApp.this.lastSelected)) == null)
						{
							material = getColor(atom.type);
						}
						MoleculeInspectorApp.this.primaryAtoms.get(MoleculeInspectorApp.this.lastSelected)
								.setMaterial(material);
					}

					for (int i = 0; i < primary.size(); i++)
					{
						Sphere m = MoleculeInspectorApp.this.primaryAtoms.get(i);
						if (m == node)
						{
							MoleculeInspectorApp.this.primaryAtoms.get(i)
									.setMaterial(MoleculeInspectorApp.this.whiteMaterial);
							MoleculeInspectorApp.this.lastSelected = i;
							break;
						}
					}
				}

			}
		});
	}

	private void handleMouse(Scene scene)
	{

		scene.setOnMouseDragged(new EventHandler<MouseEvent>()
		{
			private double mouseDeltaX;
			private double mouseDeltaY;

			@Override
			public void handle(MouseEvent me)
			{
				if (!me.isStillSincePress())
				{
					MoleculeInspectorApp.this.mouseOldX = MoleculeInspectorApp.this.mousePosX;
					MoleculeInspectorApp.this.mouseOldY = MoleculeInspectorApp.this.mousePosY;
				}
				else
				{
					MoleculeInspectorApp.this.mouseOldX = me.getSceneX();
					MoleculeInspectorApp.this.mouseOldY = me.getSceneY();
				}
				MoleculeInspectorApp.this.mousePosX = me.getSceneX();
				MoleculeInspectorApp.this.mousePosY = me.getSceneY();

				this.mouseDeltaX = (MoleculeInspectorApp.this.mousePosX - MoleculeInspectorApp.this.mouseOldX);
				this.mouseDeltaY = (MoleculeInspectorApp.this.mousePosY - MoleculeInspectorApp.this.mouseOldY);

				double modifier = 1.0;
				double modifierFactor = 0.1;

				if (me.isControlDown())
				{
					modifier = 0.1;
				}
				if (me.isShiftDown())
				{
					modifier = 10.0;
				}
				if (me.isPrimaryButtonDown())
				{
					MoleculeInspectorApp.this.moleculeGroupPrimary.ry
							.setAngle(MoleculeInspectorApp.this.moleculeGroupPrimary.ry
									.getAngle()
									- this.mouseDeltaX
											* modifierFactor
											* modifier * 2.0); // +
					MoleculeInspectorApp.this.moleculeGroupPrimary.rx
							.setAngle(MoleculeInspectorApp.this.moleculeGroupPrimary.rx
									.getAngle()
									+ this.mouseDeltaY
											* modifierFactor
											* modifier * 2.0); // -

				}
				else if (me.isSecondaryButtonDown())
				{
					double z = MoleculeInspectorApp.this.camera.getTranslateZ();
					double newZ = z + this.mouseDeltaX * modifierFactor * modifier;
					MoleculeInspectorApp.this.camera.setTranslateZ(newZ);
				}
				else if (me.isMiddleButtonDown())
				{
					MoleculeInspectorApp.this.cameraXForm2.t
							.setX(MoleculeInspectorApp.this.cameraXForm2.t.getX() + this.mouseDeltaX
									* modifierFactor * modifier * 0.3); // -
					MoleculeInspectorApp.this.cameraXForm2.t
							.setY(MoleculeInspectorApp.this.cameraXForm2.t.getY() + this.mouseDeltaY
									* modifierFactor * modifier * 0.3); // -
				}
			}
		});
	}
}
