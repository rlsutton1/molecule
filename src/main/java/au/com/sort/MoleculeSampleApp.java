package au.com.sort;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javafx.scene.Camera;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Sphere;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.SubScene;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.scene.layout.BorderPane;


public class MoleculeSampleApp extends Application {

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

	private void buildScene(Group root) {
		System.out.println("buildScene");
		root.getChildren().add(world);
	}

	@Override
	public void start(Stage primaryStage) {
		System.out.println("start");
		final Group root = new Group();
				buildScene( root);

		buildCamera( root);
		// buildAxes();
		buildMolecule();

	
	//	SubScene subScene = new SubScene(subroot, 100, 200);
		
		Scene scene = new Scene(root, 1024, 768, true);
		scene.setFill(Color.GREY);

		primaryStage.setTitle("Molecule Sample Application");
		primaryStage.setScene(scene);
		primaryStage.show();


		scene.setCamera(camera);
		
		//root.getChildren().add(buildSubScene());
		//root.getChildren().add(test());

		handleMouse(scene, root);

	}
	
	BorderPane test()
	{
		Button button = new Button("Brute");
		BorderPane borderPane = new BorderPane();
        borderPane.setStyle("-fx-border-color: black;-fx-background-color: #66CCFF;");
        borderPane.setTop(button);
        
        borderPane.setMaxSize(2, 2);
        

        return borderPane;
	}

	SubScene buildSubScene() {

		Camera camera = new PerspectiveCamera();

	     //-------------------
//	    cameraXRotate = new Rotate(-5,Rotate.X_AXIS);
//	    cameraYRotate = new Rotate(-50,Rotate.Y_AXIS);
	    Translate cameraPosition2 = new Translate(0,10,10);    
//
//	    camera.getTransforms().add(cameraXRotate);
//	    camera.getTransforms().add(cameraYRotate);
//	    camera.getTransforms().add(cameraPosition2);
		
		HBox hbox1 = new HBox();
		Button button = new Button("Brute");
		hbox1.getChildren().add(button);

		AnchorPane subroot = new AnchorPane();

		AnchorPane.setRightAnchor(hbox1, 0.0);
		AnchorPane.setBottomAnchor(hbox1,0.0);

		subroot.getChildren().add(hbox1);

		SubScene subScene = new SubScene(subroot, 100, 200);
		subScene.setCamera(camera);
		
//		subScene.setScaleX(0.2);
//		subScene.setScaleY(0.2);
//		subScene.setScaleZ(0.2);

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
			IOException, InterruptedException {
		System.setProperty("prism.dirtyopts", "false");
		String stripList = null;
		if (args.length > 4) {
			stripList = args[3];
		}
		String atomFilter = null;
		if (args.length > 2)
		{
			atomFilter = args[2];
		}

		FileLoader loader = new FileLoader(args[0], args[1], atomFilter, stripList);

		primary = loader.getPrimary();
		secondary = loader.getSecondary();

		// secondary = RelationshipSorter.getSorted(primary, secondary);
		launch(args);
	}

	private void buildCamera(Group root) {
		root.getChildren().add(cameraXForm);
		cameraXForm.getChildren().add(cameraXForm2);
		cameraXForm2.getChildren().add(cameraXForm3);
		cameraXForm3.getChildren().add(camera);
		cameraXForm3.setRotateZ(180.0);

		camera.setFieldOfView(7);
		camera.setNearClip(0.1);
		camera.setFarClip(10000.0);
		camera.setTranslateZ(-cameraDistance);
		cameraXForm.ry.setAngle(180);
		cameraXForm.rx.setAngle(180);
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

	private void buildMolecule() {

		redMaterial = new PhongMaterial();
		redMaterial.setDiffuseColor(Color.DARKRED);
		redMaterial.setSpecularColor(Color.RED);

		whiteMaterial = new PhongMaterial();
		whiteMaterial.setDiffuseColor(Color.WHITE);
		whiteMaterial.setSpecularColor(Color.LIGHTBLUE);

		greyMaterial = new PhongMaterial();
		greyMaterial.setDiffuseColor(Color.BLUE);
		greyMaterial.setSpecularColor(Color.BLUE);

		XForm moleculeXForm1 = new XForm();

		XForm moleculeXForm2 = new XForm();

		for (Atom m : primary) {
			XForm atomXForm = new XForm();

			Sphere atom = new Sphere(1.0);
			atom.setMaterial(redMaterial);
			atom.setTranslateX(m.position.getX());
			atom.setTranslateY(m.position.getY());
			atom.setTranslateZ(m.position.getZ());
			atomXForm.getChildren().add(atom);

			primaryAtoms.add(atom);
			moleculeXForm1.getChildren().add(atomXForm);
		}

		for (Atom m : secondary) {
			XForm atomXForm = new XForm();

			Sphere atom = new Sphere(1.0);
			atom.setMaterial(redMaterial);
			atom.setTranslateX(m.position.getX());
			atom.setTranslateY(m.position.getY());
			atom.setTranslateZ(m.position.getZ());
			atomXForm.getChildren().add(atom);
			moleculeXForm2.getChildren().add(atomXForm);
			secondaryAtoms.add(atom);
		}

		moleculeGroupPrimary.setTranslateX(-SEPARATION);
		moleculeGroupPrimary.getChildren().add(moleculeXForm1);

		moleculeGroupSecondary.getChildren().add(moleculeXForm2);
		moleculeGroupSecondary.setTranslateX(SEPARATION);

		world.getChildren().addAll(moleculeGroupPrimary);
		world.getChildren().addAll(moleculeGroupSecondary);
	}

	private double mousePosX;
	private double mousePosY;
	private double mouseOldX;
	private double mouseOldY;
	protected Integer lastSelected = null;

	Map<Atom, Atom> selected = new LinkedHashMap<>();

	private void handleMouse(Scene scene, final Node root) {

		moleculeGroupSecondary
				.setOnMousePressed(new EventHandler<MouseEvent>() {

					@Override
					public void handle(MouseEvent me) {
						double x = me.getX();
						double y = me.getY();
						double z = me.getZ();

						if (lastSelected != null && me.isSecondaryButtonDown()) {
							// here we can do a relationship swap!!!

							for (int i = 0; i < secondary.size(); i++) {
								Atom m = secondary.get(i);
								if (Math.abs((m.position.getX()) - x) < 1
										&& Math.abs(m.position.getY() - y) < 1
										&& Math.abs(m.position.getZ() - z) < 1) {

									// clear the colors of the currently
									// selected atoms
									secondaryAtoms.get(lastSelected)
											.setMaterial(redMaterial);
									secondaryAtoms.get(i).setMaterial(
											whiteMaterial);

									// swap the secondary atoms around
									Atom last = secondary.get(lastSelected);
									Atom other = secondary.get(i);
									secondary.add(lastSelected, other);
									secondary.remove(lastSelected + 1);
									secondary.add(i, last);
									secondary.remove(i + 1);

									Sphere lastAtom = secondaryAtoms
											.get(lastSelected);
									Sphere otherAtom = secondaryAtoms.get(i);

									secondaryAtoms.add(lastSelected, otherAtom);
									secondaryAtoms.remove(lastSelected + 1);
									secondaryAtoms.add(i, lastAtom);
									secondaryAtoms.remove(i + 1);

									break;
								}
							}

						}

					}
				});
		moleculeGroupPrimary.setOnMousePressed(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent me) {
				mousePosX = me.getSceneX();
				mousePosY = me.getSceneY();
				mouseOldX = me.getSceneX();
				mouseOldY = me.getSceneY();

				System.out.println(me.getX() + " " + me.getY() + "  "
						+ me.getZ());

				double x = me.getX();
				double y = me.getY();
				double z = me.getZ();

				if (me.getClickCount() == 1) {
					if (lastSelected != null) {
						PhongMaterial material = greyMaterial;
						if (selected.get(primary.get(lastSelected)) == null) {
							material = redMaterial;
						}
						primaryAtoms.get(lastSelected).setMaterial(material);
						secondaryAtoms.get(lastSelected).setMaterial(material);
					}

					for (int i = 0; i < primary.size(); i++) {
						Atom m = primary.get(i);
						if (Math.abs((m.position.getX()) - x) < 1
								&& Math.abs(m.position.getY() - y) < 1
								&& Math.abs(m.position.getZ() - z) < 1) {
							primaryAtoms.get(i).setMaterial(whiteMaterial);
							secondaryAtoms.get(i).setMaterial(whiteMaterial);
							System.out.println("Matched");
							lastSelected = i;
							break;
						}
					}
				}

				if (me.getClickCount() == 2) {
					if (lastSelected != null
							&& primary.get(lastSelected) != null) {
						if (selected.get(primary.get(lastSelected)) == null) {
							selected.put(primary.get(lastSelected),
									secondary.get(lastSelected));
							System.out.println("double");
							primaryAtoms.get(lastSelected).setMaterial(
									greyMaterial);
							secondaryAtoms.get(lastSelected).setMaterial(
									greyMaterial);

						} else {
							{
								selected.remove(primary.get(lastSelected));
								System.out.println("double");
								primaryAtoms.get(lastSelected).setMaterial(
										redMaterial);
								secondaryAtoms.get(lastSelected).setMaterial(
										redMaterial);

							}
						}
					}
				}

				for (Atom t : selected.keySet()) {
					System.out.println(t + " " + selected.get(t));
				}

			}
		});
		scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
			private double mouseDeltaX;
			private double mouseDeltaY;

			@Override
			public void handle(MouseEvent me) {
				if (!me.isStillSincePress())
				{
				mouseOldX = mousePosX;
				mouseOldY = mousePosY;
				}else
				{
					mouseOldX = me.getSceneX();
					mouseOldY = me.getSceneY();
				}
				mousePosX = me.getSceneX();
				mousePosY = me.getSceneY();

				mouseDeltaX = (mousePosX - mouseOldX);
				mouseDeltaY = (mousePosY - mouseOldY);

				double modifier = 1.0;
				double modifierFactor = 0.1;

				if (me.isControlDown()) {
					modifier = 0.1;
				}
				if (me.isShiftDown()) {
					modifier = 10.0;
				}
				if (me.isPrimaryButtonDown()) {
					moleculeGroupPrimary.ry.setAngle(moleculeGroupPrimary.ry
							.getAngle()
							- mouseDeltaX
							* modifierFactor
							* modifier * 2.0); // +
					moleculeGroupPrimary.rx.setAngle(moleculeGroupPrimary.rx
							.getAngle()
							+ mouseDeltaY
							* modifierFactor
							* modifier * 2.0); // -

					moleculeGroupSecondary.ry
							.setAngle(moleculeGroupSecondary.ry.getAngle()
									- mouseDeltaX * modifierFactor * modifier
									* 2.0); // +
					moleculeGroupSecondary.rx
							.setAngle(moleculeGroupSecondary.rx.getAngle()
									+ mouseDeltaY * modifierFactor * modifier
									* 2.0); // -

				} else if (me.isSecondaryButtonDown()) {
					double z = camera.getTranslateZ();
					double newZ = z + mouseDeltaX * modifierFactor * modifier;
					camera.setTranslateZ(newZ);
				} else if (me.isMiddleButtonDown()) {
					cameraXForm2.t.setX(cameraXForm2.t.getX() + mouseDeltaX
							* modifierFactor * modifier * 0.3); // -
					cameraXForm2.t.setY(cameraXForm2.t.getY() + mouseDeltaY
							* modifierFactor * modifier * 0.3); // -
				}
			}
		});
	}
}
