package au.com.sort;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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

public class MoleculeSampleApp extends Application
{

	private static final int SEPARATION = 18;
	private static List<Atom> primary;
	private static List<Atom> secondary;

	final XForm world = new XForm();

	final PerspectiveCamera camera = new PerspectiveCamera(true);
	final XForm cameraXForm = new XForm();
	final XForm cameraXForm2 = new XForm();
	final XForm cameraXForm3 = new XForm();
	final double cameraDistance = 400;

	final XForm moleculeGroupPrimary = new XForm();
	final XForm moleculeGroupSecondary = new XForm();

	// final Group axisGroup = new Group();
	private PhongMaterial whiteMaterial;

	List<Sphere> primaryAtoms = new LinkedList<>();
	List<Sphere> secondaryAtoms = new LinkedList<>();
	private PhongMaterial redMaterial;
	private PhongMaterial greyMaterial;

	private void buildScene(Group root)
	{
		System.out.println("buildScene");
		root.getChildren().add(this.world);
	}

	@Override
	public void start(Stage primaryStage)
	{
		System.out.println("start");
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

		FileLoader loader = new FileLoader(args[0], args[1], atomFilter,
				stripList);

		primary = loader.getPrimary();
		secondary = loader.getSecondary();

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

	private void buildMolecule()
	{

		this.redMaterial = new PhongMaterial();
		this.redMaterial.setDiffuseColor(Color.DARKRED);
		this.redMaterial.setSpecularColor(Color.RED);

		this.whiteMaterial = new PhongMaterial();
		this.whiteMaterial.setDiffuseColor(Color.WHITE);
		this.whiteMaterial.setSpecularColor(Color.LIGHTBLUE);

		this.greyMaterial = new PhongMaterial();
		this.greyMaterial.setDiffuseColor(Color.BLUE);
		this.greyMaterial.setSpecularColor(Color.BLUE);

		XForm moleculeXForm1 = new XForm();

		XForm moleculeXForm2 = new XForm();

		for (Atom m : primary)
		{
			XForm atomXForm = new XForm();

			Sphere atom = new Sphere(1.0);
			atom.setMaterial(this.redMaterial);
			atom.setTranslateX(m.position.getX());
			atom.setTranslateY(m.position.getY());
			atom.setTranslateZ(m.position.getZ());
			atomXForm.getChildren().add(atom);

			this.primaryAtoms.add(atom);
			moleculeXForm1.getChildren().add(atomXForm);
			addPrimaryMouseHandler(atom);
		}

		for (Atom m : secondary)
		{
			XForm atomXForm = new XForm();

			Sphere atom = new Sphere(1.0);
			atom.setMaterial(this.redMaterial);
			atom.setTranslateX(m.position.getX());
			atom.setTranslateY(m.position.getY());
			atom.setTranslateZ(m.position.getZ());
			atomXForm.getChildren().add(atom);
			moleculeXForm2.getChildren().add(atomXForm);
			this.secondaryAtoms.add(atom);
			addSecondaryMouseHandler(atom);
		}

		this.moleculeGroupPrimary.setTranslateX(-SEPARATION);
		this.moleculeGroupPrimary.getChildren().add(moleculeXForm1);

		this.moleculeGroupSecondary.getChildren().add(moleculeXForm2);
		this.moleculeGroupSecondary.setTranslateX(SEPARATION);

		this.world.getChildren().addAll(this.moleculeGroupPrimary);
		this.world.getChildren().addAll(this.moleculeGroupSecondary);
	}

	private double mousePosX;
	private double mousePosY;
	private double mouseOldX;
	private double mouseOldY;
	protected Integer lastSelected = null;

	Map<Atom, Atom> selected = new LinkedHashMap<>();

	private void addSecondaryMouseHandler(final Sphere node)
	{
		node.setOnMousePressed(new EventHandler<MouseEvent>()
		{

			@Override
			public void handle(MouseEvent me)
			{

				if (MoleculeSampleApp.this.lastSelected != null && me.isSecondaryButtonDown())
				{
					// here we can do a relationship swap!!!

					for (int i = 0; i < MoleculeSampleApp.this.secondaryAtoms.size(); i++)
					{
						Sphere m = MoleculeSampleApp.this.secondaryAtoms.get(i);

						if (m == node)
						{

							// clear the colors of the currently
							// selected atoms
							MoleculeSampleApp.this.secondaryAtoms.get(MoleculeSampleApp.this.lastSelected).setMaterial(
									MoleculeSampleApp.this.redMaterial);
							MoleculeSampleApp.this.secondaryAtoms.get(i)
									.setMaterial(MoleculeSampleApp.this.whiteMaterial);

							// swap the secondary atoms around
							Atom last = secondary.get(MoleculeSampleApp.this.lastSelected);
							Atom other = secondary.get(i);
							secondary.add(MoleculeSampleApp.this.lastSelected, other);
							secondary.remove(MoleculeSampleApp.this.lastSelected + 1);
							secondary.add(i, last);
							secondary.remove(i + 1);

							Sphere lastAtom = MoleculeSampleApp.this.secondaryAtoms
									.get(MoleculeSampleApp.this.lastSelected);
							Sphere otherAtom = MoleculeSampleApp.this.secondaryAtoms.get(i);

							MoleculeSampleApp.this.secondaryAtoms.add(MoleculeSampleApp.this.lastSelected, otherAtom);
							MoleculeSampleApp.this.secondaryAtoms.remove(MoleculeSampleApp.this.lastSelected + 1);
							MoleculeSampleApp.this.secondaryAtoms.add(i, lastAtom);
							MoleculeSampleApp.this.secondaryAtoms.remove(i + 1);

							break;
						}
					}

				}

			}
		});
	}

	void addPrimaryMouseHandler(final Sphere node)
	{
		node.setOnMousePressed(new EventHandler<MouseEvent>()
		{

			@Override
			public void handle(MouseEvent me)
			{
				MoleculeSampleApp.this.mousePosX = me.getSceneX();
				MoleculeSampleApp.this.mousePosY = me.getSceneY();
				MoleculeSampleApp.this.mouseOldX = me.getSceneX();
				MoleculeSampleApp.this.mouseOldY = me.getSceneY();

				System.out.println(me.getX() + " " + me.getY() + "  "
						+ me.getZ());

				if (me.getClickCount() == 1)
				{
					if (MoleculeSampleApp.this.lastSelected != null)
					{
						PhongMaterial material = MoleculeSampleApp.this.greyMaterial;
						if (MoleculeSampleApp.this.selected
								.get(primary.get(MoleculeSampleApp.this.lastSelected)) == null)
						{
							material = MoleculeSampleApp.this.redMaterial;
						}
						MoleculeSampleApp.this.primaryAtoms.get(MoleculeSampleApp.this.lastSelected)
								.setMaterial(material);
						MoleculeSampleApp.this.secondaryAtoms.get(MoleculeSampleApp.this.lastSelected)
								.setMaterial(material);
					}

					for (int i = 0; i < primary.size(); i++)
					{
						Sphere m = MoleculeSampleApp.this.primaryAtoms.get(i);
						if (m == node)
						{
							MoleculeSampleApp.this.primaryAtoms.get(i)
									.setMaterial(MoleculeSampleApp.this.whiteMaterial);
							MoleculeSampleApp.this.secondaryAtoms.get(i)
									.setMaterial(MoleculeSampleApp.this.whiteMaterial);
							System.out.println("Matched");
							MoleculeSampleApp.this.lastSelected = i;
							break;
						}
					}
				}

				if (me.getClickCount() == 2)
				{
					if (MoleculeSampleApp.this.lastSelected != null
							&& primary.get(MoleculeSampleApp.this.lastSelected) != null)
					{
						if (MoleculeSampleApp.this.selected
								.get(primary.get(MoleculeSampleApp.this.lastSelected)) == null)
						{
							MoleculeSampleApp.this.selected.put(primary.get(MoleculeSampleApp.this.lastSelected),
									secondary.get(MoleculeSampleApp.this.lastSelected));
							System.out.println("double");
							MoleculeSampleApp.this.primaryAtoms.get(MoleculeSampleApp.this.lastSelected).setMaterial(
									MoleculeSampleApp.this.greyMaterial);
							MoleculeSampleApp.this.secondaryAtoms.get(MoleculeSampleApp.this.lastSelected).setMaterial(
									MoleculeSampleApp.this.greyMaterial);

						}
						else
						{
							{
								MoleculeSampleApp.this.selected
										.remove(primary.get(MoleculeSampleApp.this.lastSelected));
								System.out.println("double");
								MoleculeSampleApp.this.primaryAtoms.get(MoleculeSampleApp.this.lastSelected)
										.setMaterial(
												MoleculeSampleApp.this.redMaterial);
								MoleculeSampleApp.this.secondaryAtoms.get(MoleculeSampleApp.this.lastSelected)
										.setMaterial(
												MoleculeSampleApp.this.redMaterial);

							}
						}
					}
				}

				for (Atom t : MoleculeSampleApp.this.selected.keySet())
				{
					System.out.println(t + " " + MoleculeSampleApp.this.selected.get(t));
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
					MoleculeSampleApp.this.mouseOldX = MoleculeSampleApp.this.mousePosX;
					MoleculeSampleApp.this.mouseOldY = MoleculeSampleApp.this.mousePosY;
				}
				else
				{
					MoleculeSampleApp.this.mouseOldX = me.getSceneX();
					MoleculeSampleApp.this.mouseOldY = me.getSceneY();
				}
				MoleculeSampleApp.this.mousePosX = me.getSceneX();
				MoleculeSampleApp.this.mousePosY = me.getSceneY();

				this.mouseDeltaX = (MoleculeSampleApp.this.mousePosX - MoleculeSampleApp.this.mouseOldX);
				this.mouseDeltaY = (MoleculeSampleApp.this.mousePosY - MoleculeSampleApp.this.mouseOldY);

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
					MoleculeSampleApp.this.moleculeGroupPrimary.ry
							.setAngle(MoleculeSampleApp.this.moleculeGroupPrimary.ry
									.getAngle()
									- this.mouseDeltaX
											* modifierFactor
											* modifier * 2.0); // +
					MoleculeSampleApp.this.moleculeGroupPrimary.rx
							.setAngle(MoleculeSampleApp.this.moleculeGroupPrimary.rx
									.getAngle()
									+ this.mouseDeltaY
											* modifierFactor
											* modifier * 2.0); // -

					MoleculeSampleApp.this.moleculeGroupSecondary.ry
							.setAngle(MoleculeSampleApp.this.moleculeGroupSecondary.ry.getAngle()
									- this.mouseDeltaX * modifierFactor * modifier
											* 2.0); // +
					MoleculeSampleApp.this.moleculeGroupSecondary.rx
							.setAngle(MoleculeSampleApp.this.moleculeGroupSecondary.rx.getAngle()
									+ this.mouseDeltaY * modifierFactor * modifier
											* 2.0); // -

				}
				else if (me.isSecondaryButtonDown())
				{
					double z = MoleculeSampleApp.this.camera.getTranslateZ();
					double newZ = z + this.mouseDeltaX * modifierFactor * modifier;
					MoleculeSampleApp.this.camera.setTranslateZ(newZ);
				}
				else if (me.isMiddleButtonDown())
				{
					MoleculeSampleApp.this.cameraXForm2.t
							.setX(MoleculeSampleApp.this.cameraXForm2.t.getX() + this.mouseDeltaX
									* modifierFactor * modifier * 0.3); // -
					MoleculeSampleApp.this.cameraXForm2.t
							.setY(MoleculeSampleApp.this.cameraXForm2.t.getY() + this.mouseDeltaY
									* modifierFactor * modifier * 0.3); // -
				}
			}
		});
	}
}
