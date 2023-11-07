package au.com.sort;

// 
//Xform.java is a class that extends the Group class. It is used in the 
//MoleculeSampleApp application that is built using the Getting Started with JavaFX
//3D Graphics tutorial. The method allows you to add your own transforms and rotation.
//

import javafx.scene.Group;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;

public class XForm extends Group
{

	public enum RotateOrder
	{
		XYZ, XZY, YXZ, YZX, ZXY, ZYX
	}

	public Translate t = new Translate();
	public Translate p = new Translate();
	public Translate ip = new Translate();
	public Rotate rx = new Rotate();
	{
		this.rx.setAxis(Rotate.X_AXIS);
	}
	public Rotate ry = new Rotate();
	{
		this.ry.setAxis(Rotate.Y_AXIS);
	}
	public Rotate rz = new Rotate();
	{
		this.rz.setAxis(Rotate.Z_AXIS);
	}
	public Scale s = new Scale();

	public XForm()
	{
		super();
		getTransforms().addAll(this.t, this.rz, this.ry, this.rx, this.s);
	}

	public XForm(RotateOrder rotateOrder)
	{
		super();
		// choose the order of rotations based on the rotateOrder
		switch (rotateOrder)
		{
			case XYZ:
				getTransforms().addAll(this.t, this.p, this.rz, this.ry, this.rx, this.s, this.ip);
				break;
			case XZY:
				getTransforms().addAll(this.t, this.p, this.ry, this.rz, this.rx, this.s, this.ip);
				break;
			case YXZ:
				getTransforms().addAll(this.t, this.p, this.rz, this.rx, this.ry, this.s, this.ip);
				break;
			case YZX:
				getTransforms().addAll(this.t, this.p, this.rx, this.rz, this.ry, this.s, this.ip); // For Camera
				break;
			case ZXY:
				getTransforms().addAll(this.t, this.p, this.ry, this.rx, this.rz, this.s, this.ip);
				break;
			case ZYX:
				getTransforms().addAll(this.t, this.p, this.rx, this.ry, this.rz, this.s, this.ip);
				break;
		}
	}

	public void setTranslate(double x, double y, double z)
	{
		this.t.setX(x);
		this.t.setY(y);
		this.t.setZ(z);
	}

	public void setTranslate(double x, double y)
	{
		this.t.setX(x);
		this.t.setY(y);
	}

	// Cannot override these methods as they are final:
	// public void setTranslateX(double x) { t.setX(x); }
	// public void setTranslateY(double y) { t.setY(y); }
	// public void setTranslateZ(double z) { t.setZ(z); }
	// Use these methods instead:
	public void setTx(double x)
	{
		this.t.setX(x);
	}

	public void setTy(double y)
	{
		this.t.setY(y);
	}

	public void setTz(double z)
	{
		this.t.setZ(z);
	}

	public void setRotate(double x, double y, double z)
	{
		this.rx.setAngle(x);
		this.ry.setAngle(y);
		this.rz.setAngle(z);
	}

	public void setRotateX(double x)
	{
		this.rx.setAngle(x);
	}

	public void setRotateY(double y)
	{
		this.ry.setAngle(y);
	}

	public void setRotateZ(double z)
	{
		this.rz.setAngle(z);
	}

	public void setRx(double x)
	{
		this.rx.setAngle(x);
	}

	public void setRy(double y)
	{
		this.ry.setAngle(y);
	}

	public void setRz(double z)
	{
		this.rz.setAngle(z);
	}

	public void setScale(double scaleFactor)
	{
		this.s.setX(scaleFactor);
		this.s.setY(scaleFactor);
		this.s.setZ(scaleFactor);
	}

	public void setScale(double x, double y, double z)
	{
		this.s.setX(x);
		this.s.setY(y);
		this.s.setZ(z);
	}

	// Cannot override these methods as they are final:
	// public void setScaleX(double x) { s.setX(x); }
	// public void setScaleY(double y) { s.setY(y); }
	// public void setScaleZ(double z) { s.setZ(z); }
	// Use these methods instead:
	public void setSx(double x)
	{
		this.s.setX(x);
	}

	public void setSy(double y)
	{
		this.s.setY(y);
	}

	public void setSz(double z)
	{
		this.s.setZ(z);
	}

	public void setPivot(double x, double y, double z)
	{
		this.p.setX(x);
		this.p.setY(y);
		this.p.setZ(z);
		this.ip.setX(-x);
		this.ip.setY(-y);
		this.ip.setZ(-z);
	}

	public void reset()
	{
		this.t.setX(0.0);
		this.t.setY(0.0);
		this.t.setZ(0.0);
		this.rx.setAngle(0.0);
		this.ry.setAngle(0.0);
		this.rz.setAngle(0.0);
		this.s.setX(1.0);
		this.s.setY(1.0);
		this.s.setZ(1.0);
		this.p.setX(0.0);
		this.p.setY(0.0);
		this.p.setZ(0.0);
		this.ip.setX(0.0);
		this.ip.setY(0.0);
		this.ip.setZ(0.0);
	}

	public void resetTSP()
	{
		this.t.setX(0.0);
		this.t.setY(0.0);
		this.t.setZ(0.0);
		this.s.setX(1.0);
		this.s.setY(1.0);
		this.s.setZ(1.0);
		this.p.setX(0.0);
		this.p.setY(0.0);
		this.p.setZ(0.0);
		this.ip.setX(0.0);
		this.ip.setY(0.0);
		this.ip.setZ(0.0);
	}

	public void debug()
	{
		System.out.println("t = (" + this.t.getX() + ", " + this.t.getY() + ", "
				+ this.t.getZ() + ")  " + "r = (" + this.rx.getAngle() + ", "
				+ this.ry.getAngle() + ", " + this.rz.getAngle() + ")  " + "s = ("
				+ this.s.getX() + ", " + this.s.getY() + ", " + this.s.getZ() + ")  "
				+ "p = (" + this.p.getX() + ", " + this.p.getY() + ", " + this.p.getZ()
				+ ")  " + "ip = (" + this.ip.getX() + ", " + this.ip.getY() + ", "
				+ this.ip.getZ() + ")");
	}
}
