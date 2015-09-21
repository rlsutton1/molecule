package au.com.sort;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Sphere;
import javafx.stage.Stage;

public class MoleculeSampleApp extends Application
{

	private static final int SEPARATION = 18;
	private static List<Molecule> primary;
	private static List<Molecule> secondary;
	final Group root = new Group();
	final XForm world = new XForm();

	final PerspectiveCamera camera = new PerspectiveCamera(true);
	final XForm cameraXForm = new XForm();
	final XForm cameraXForm2 = new XForm();
	final XForm cameraXForm3 = new XForm();
	final double cameraDistance = 100;

	final XForm moleculeGroup = new XForm();

	final Group axisGroup = new Group();
	private PhongMaterial whiteMaterial;

	List<Sphere> primaryAtoms = new LinkedList<>();
	List<Sphere> secondaryAtoms = new LinkedList<>();
	private PhongMaterial redMaterial;
	private PhongMaterial greyMaterial;

	private void buildScene()
	{
		System.out.println("buildScene");
		root.getChildren().add(world);
	}

	@Override
	public void start(Stage primaryStage)
	{
		System.out.println("start");
		buildScene();

		buildCamera();
		buildAxes();
		buildMolecule();

		Scene scene = new Scene(root, 1024, 768, true);
		scene.setFill(Color.GREY);

		primaryStage.setTitle("Molecule Sample Application");
		primaryStage.setScene(scene);
		primaryStage.show();

		scene.setCamera(camera);

		handleMouse(scene, root);

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

		FileLoader loader = new FileLoader(args[0], args[1], args[2], args[3]);

		primary = loader.getPrimary();
		secondary = loader.getSecondary();

		secondary = RelationshipSorter.getSorted(primary, secondary);
		launch(args);
	}

	private void buildCamera()
	{
		root.getChildren().add(cameraXForm);
		cameraXForm.getChildren().add(cameraXForm2);
		cameraXForm2.getChildren().add(cameraXForm3);
		cameraXForm3.getChildren().add(camera);
		cameraXForm3.setRotateZ(180.0);

		camera.setNearClip(0.1);
		camera.setFarClip(10000.0);
		camera.setTranslateZ(-cameraDistance);
		cameraXForm.ry.setAngle(320.0);
		cameraXForm.rx.setAngle(40);
	}

	private void buildAxes()
	{
		System.out.println("buildAxes()");
		final PhongMaterial redMaterial = new PhongMaterial();
		redMaterial.setDiffuseColor(Color.DARKRED);
		redMaterial.setSpecularColor(Color.RED);

		final PhongMaterial greenMaterial = new PhongMaterial();
		greenMaterial.setDiffuseColor(Color.DARKGREEN);
		greenMaterial.setSpecularColor(Color.GREEN);

		final PhongMaterial blueMaterial = new PhongMaterial();
		blueMaterial.setDiffuseColor(Color.DARKBLUE);
		blueMaterial.setSpecularColor(Color.BLUE);

		final Box xAxis = new Box(240.0, 1, 1);
		final Box yAxis = new Box(1, 240.0, 1);
		final Box zAxis = new Box(1, 1, 240.0);

		xAxis.setMaterial(redMaterial);
		yAxis.setMaterial(greenMaterial);
		zAxis.setMaterial(blueMaterial);

		axisGroup.getChildren().addAll(xAxis, yAxis, zAxis);
		world.getChildren().addAll(axisGroup);
	}

	private void buildMolecule()
	{

		redMaterial = new PhongMaterial();
		redMaterial.setDiffuseColor(Color.DARKRED);
		redMaterial.setSpecularColor(Color.RED);

		whiteMaterial = new PhongMaterial();
		whiteMaterial.setDiffuseColor(Color.WHITE);
		whiteMaterial.setSpecularColor(Color.LIGHTBLUE);

		greyMaterial = new PhongMaterial();
		greyMaterial.setDiffuseColor(Color.BLUE);
		greyMaterial.setSpecularColor(Color.BLUE);

		XForm moleculeXForm = new XForm();

		for (Molecule m : primary)
		{
			XForm atomXForm = new XForm();

			Sphere atom = new Sphere(1.0);
			atom.setMaterial(redMaterial);
			atom.setTranslateX(m.position.getX() - SEPARATION);
			atom.setTranslateY(m.position.getY());
			atom.setTranslateZ(m.position.getZ());
			atomXForm.getChildren().add(atom);

			primaryAtoms.add(atom);
			moleculeXForm.getChildren().add(atomXForm);
		}

		for (Molecule m : secondary)
		{
			XForm atomXForm = new XForm();

			Sphere atom = new Sphere(1.0);
			atom.setMaterial(redMaterial);
			atom.setTranslateX(m.position.getX() + SEPARATION);
			atom.setTranslateY(m.position.getY());
			atom.setTranslateZ(m.position.getZ());
			atomXForm.getChildren().add(atom);
			moleculeXForm.getChildren().add(atomXForm);
			secondaryAtoms.add(atom);
		}

		moleculeGroup.getChildren().add(moleculeXForm);

		world.getChildren().addAll(moleculeGroup);
	}

	private double mousePosX;
	private double mousePosY;
	private double mouseOldX;
	private double mouseOldY;
	protected Integer lastSelected = null;

	Map<Molecule, Molecule> selected = new LinkedHashMap<>();

	private void handleMouse(Scene scene, final Node root)
	{
		scene.setOnMousePressed(new EventHandler<MouseEvent>()
		{

			@Override
			public void handle(MouseEvent me)
			{
				mousePosX = me.getSceneX();
				mousePosY = me.getSceneY();
				mouseOldX = me.getSceneX();
				mouseOldY = me.getSceneY();

				System.out.println(me.getX() + " " + me.getY() + "  "
						+ me.getZ());

				double x = me.getX();
				double y = me.getY();
				double z = me.getZ();

				if (me.getClickCount() == 1)
				{
					if (lastSelected != null)
					{
						if (selected.get(primary.get(lastSelected)) != null)
						{
							primaryAtoms.get(lastSelected).setMaterial(
									greyMaterial);
							secondaryAtoms.get(lastSelected).setMaterial(
									greyMaterial);
						} else
						{
							primaryAtoms.get(lastSelected).setMaterial(
									redMaterial);
							secondaryAtoms.get(lastSelected).setMaterial(
									redMaterial);
						}
					}

					for (int i = 0; i < primary.size(); i++)
					{
						Molecule m = primary.get(i);
						if (Math.abs((m.position.getX() - SEPARATION) - x) < 1
								&& Math.abs(m.position.getY() - y) < 1
								&& Math.abs(m.position.getZ() - z) < 1)
						{
							primaryAtoms.get(i).setMaterial(whiteMaterial);
							secondaryAtoms.get(i).setMaterial(whiteMaterial);
							System.out.println("Matched");
							lastSelected = i;
							break;
						}
					}
				}
				if (me.getClickCount() == 2)
				{
					if (selected.get(primary.get(lastSelected)) == null)
					{
						selected.put(primary.get(lastSelected),
								secondary.get(lastSelected));
						System.out.println("double");
						primaryAtoms.get(lastSelected)
								.setMaterial(greyMaterial);
						secondaryAtoms.get(lastSelected).setMaterial(
								greyMaterial);
						
					} else
					{
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

				for (Molecule t : selected.keySet())
				{
					System.out.println(t + " " + selected.get(t));
				}

			}
		});
		scene.setOnMouseDragged(new EventHandler<MouseEvent>()
		{
			private double mouseDeltaX;
			private double mouseDeltaY;

			@Override
			public void handle(MouseEvent me)
			{
				mouseOldX = mousePosX;
				mouseOldY = mousePosY;
				mousePosX = me.getSceneX();
				mousePosY = me.getSceneY();

				mouseDeltaX = (mousePosX - mouseOldX);
				mouseDeltaY = (mousePosY - mouseOldY);

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
					cameraXForm.ry.setAngle(cameraXForm.ry.getAngle()
							- mouseDeltaX * modifierFactor * modifier * 2.0); // +
					cameraXForm.rx.setAngle(cameraXForm.rx.getAngle()
							+ mouseDeltaY * modifierFactor * modifier * 2.0); // -
				} else if (me.isSecondaryButtonDown())
				{
					double z = camera.getTranslateZ();
					double newZ = z + mouseDeltaX * modifierFactor * modifier;
					camera.setTranslateZ(newZ);
				} else if (me.isMiddleButtonDown())
				{
					cameraXForm2.t.setX(cameraXForm2.t.getX() + mouseDeltaX
							* modifierFactor * modifier * 0.3); // -
					cameraXForm2.t.setY(cameraXForm2.t.getY() + mouseDeltaY
							* modifierFactor * modifier * 0.3); // -
				}
			}
		});
	}
}
