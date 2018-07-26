package gate.resources.img.svg;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * This class has been automatically generated using <a
 * href="http://englishjavadrinker.blogspot.com/search/label/SVGRoundTrip">SVGRoundTrip</a>.
 */
@SuppressWarnings("unused")
public class CSVFileIcon implements
		javax.swing.Icon {
		
	private static Color getColor(int red, int green, int blue, int alpha, boolean disabled) {
		
		if (!disabled) return new Color(red, green, blue, alpha);
		
		int gray = (int)(((0.30f * red) + (0.59f * green) + (0.11f * blue))/3f);
		
		gray = Math.min(255, Math.max(0, gray));
		
		//This brightens the image the same as GrayFilter
		int percent = 50;		
		gray = (255 - ((255 - gray) * (100 - percent) / 100));

		return new Color(gray, gray, gray, alpha);
	}
	
	/**
	 * Paints the transcoded SVG image on the specified graphics context. You
	 * can install a custom transformation on the graphics context to scale the
	 * image.
	 * 
	 * @param g
	 *            Graphics context.
	 */
	public static void paint(Graphics2D g, boolean disabled) {
        Shape shape = null;
        Paint paint = null;
        Stroke stroke = null;
        Area clip = null;
         
        float origAlpha = 1.0f;
        Composite origComposite = g.getComposite();
        if (origComposite instanceof AlphaComposite) {
            AlphaComposite origAlphaComposite = 
                (AlphaComposite)origComposite;
            if (origAlphaComposite.getRule() == AlphaComposite.SRC_OVER) {
                origAlpha = origAlphaComposite.getAlpha();
            }
        }
        
	    Shape clip_ = g.getClip();
AffineTransform defaultTransform_ = g.getTransform();
//  is CompositeGraphicsNode
float alpha__0 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0 = g.getClip();
AffineTransform defaultTransform__0 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, -0.0f, 1.9073486328125E-6f));
clip = new Area(g.getClip());
clip.intersect(new Area(new Rectangle2D.Double(0.0,-1.9073486328125E-6,64.0,64.0)));
g.setClip(clip);
// _0 is CompositeGraphicsNode
float alpha__0_0 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0 = g.getClip();
AffineTransform defaultTransform__0_0 = g.getTransform();
g.transform(new AffineTransform(0.1094309389591217f, 0.0f, 0.0f, 0.1094309389591217f, 1.9282457828521729f, 1.9999849796295166f));
// _0_0 is CompositeGraphicsNode
float alpha__0_0_0 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_0_0 = g.getClip();
AffineTransform defaultTransform__0_0_0 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_0 is ShapeNode
paint = getColor(0, 0, 0, 255, disabled);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(486.2, 196.121);
((GeneralPath)shape).lineTo(473.036, 196.121);
((GeneralPath)shape).lineTo(473.036, 132.59);
((GeneralPath)shape).curveTo(473.036, 132.191, 472.97202, 131.795, 472.92, 131.39);
((GeneralPath)shape).curveTo(472.89902, 128.87, 472.096, 126.39, 470.36902, 124.43);
((GeneralPath)shape).lineTo(364.656, 3.677);
((GeneralPath)shape).curveTo(364.625, 3.643, 364.592, 3.633, 364.571, 3.602);
((GeneralPath)shape).curveTo(363.942, 2.895, 363.207, 2.31, 362.43, 1.806);
((GeneralPath)shape).curveTo(362.199, 1.649, 361.968, 1.52, 361.726, 1.387);
((GeneralPath)shape).curveTo(361.054, 1.022, 360.34, 0.715, 359.605, 0.494);
((GeneralPath)shape).curveTo(359.406, 0.442, 359.228, 0.36, 359.029, 0.306);
((GeneralPath)shape).curveTo(358.229, 0.118, 357.4, 0.0, 356.562, 0.0);
((GeneralPath)shape).lineTo(96.757, 0.0);
((GeneralPath)shape).curveTo(84.893, 0.0, 75.256, 9.649, 75.256, 21.502);
((GeneralPath)shape).lineTo(75.256, 196.115);
((GeneralPath)shape).lineTo(62.092995, 196.115);
((GeneralPath)shape).curveTo(45.120995, 196.115, 31.359995, 209.871, 31.359995, 226.845);
((GeneralPath)shape).lineTo(31.359995, 386.655);
((GeneralPath)shape).curveTo(31.359995, 403.621, 45.120995, 417.391, 62.092995, 417.391);
((GeneralPath)shape).lineTo(75.256, 417.391);
((GeneralPath)shape).lineTo(75.256, 526.79);
((GeneralPath)shape).curveTo(75.256, 538.644, 84.893, 548.29095, 96.756996, 548.29095);
((GeneralPath)shape).lineTo(451.534, 548.29095);
((GeneralPath)shape).curveTo(463.387, 548.29095, 473.036, 538.644, 473.036, 526.79);
((GeneralPath)shape).lineTo(473.036, 417.39197);
((GeneralPath)shape).lineTo(486.2, 417.39197);
((GeneralPath)shape).curveTo(503.16602, 417.39197, 516.929, 403.62796, 516.929, 386.66098);
((GeneralPath)shape).lineTo(516.929, 226.85098);
((GeneralPath)shape).curveTo(516.93, 209.87198, 503.16602, 196.12099, 486.2, 196.12099);
((GeneralPath)shape).closePath();
((GeneralPath)shape).moveTo(96.757, 21.502);
((GeneralPath)shape).lineTo(345.81, 21.502);
((GeneralPath)shape).lineTo(345.81, 131.508);
((GeneralPath)shape).curveTo(345.81, 137.448, 350.628, 142.259, 356.561, 142.259);
((GeneralPath)shape).lineTo(451.534, 142.259);
((GeneralPath)shape).lineTo(451.534, 196.12);
((GeneralPath)shape).lineTo(96.75699, 196.12);
((GeneralPath)shape).lineTo(96.75699, 21.501999);
((GeneralPath)shape).closePath();
((GeneralPath)shape).moveTo(258.618, 313.18);
((GeneralPath)shape).curveTo(231.93802, 303.88898, 214.55501, 289.12698, 214.55501, 265.791);
((GeneralPath)shape).curveTo(214.55501, 238.387, 237.41602, 217.42299, 275.28802, 217.42299);
((GeneralPath)shape).curveTo(293.39502, 217.42299, 306.73502, 221.234, 316.256, 225.52998);
((GeneralPath)shape).lineTo(308.16602, 254.82999);
((GeneralPath)shape).curveTo(301.73602, 251.72299, 290.30402, 247.19798, 274.57602, 247.19798);
((GeneralPath)shape).curveTo(258.859, 247.19798, 251.23701, 254.34698, 251.23701, 262.68298);
((GeneralPath)shape).curveTo(251.23701, 272.93, 260.28403, 277.452, 281.01703, 285.31497);
((GeneralPath)shape).curveTo(309.35803, 295.79398, 322.69803, 310.554, 322.69803, 333.18896);
((GeneralPath)shape).curveTo(322.69803, 360.09796, 301.97702, 382.97498, 257.90604, 382.97498);
((GeneralPath)shape).curveTo(239.56804, 382.97498, 221.45703, 378.19897, 212.40903, 373.205);
((GeneralPath)shape).lineTo(219.78903, 343.189);
((GeneralPath)shape).curveTo(229.56104, 348.203, 244.56403, 353.195, 260.05304, 353.195);
((GeneralPath)shape).curveTo(276.72403, 353.195, 285.54105, 346.28702, 285.54105, 335.799);
((GeneralPath)shape).curveTo(285.53604, 325.789, 277.90906, 320.078, 258.61804, 313.18002);
((GeneralPath)shape).closePath();
((GeneralPath)shape).moveTo(69.474, 302.692);
((GeneralPath)shape).curveTo(69.474, 247.91098, 108.548004, 217.42299, 157.12799, 217.42299);
((GeneralPath)shape).curveTo(175.95, 217.42299, 190.241, 221.234, 196.67699, 224.57199);
((GeneralPath)shape).lineTo(189.28499, 253.38799);
((GeneralPath)shape).curveTo(181.90498, 250.30399, 171.65298, 247.44899, 158.79399, 247.44899);
((GeneralPath)shape).curveTo(129.97198, 247.44899, 107.58799, 264.82397, 107.58799, 300.54797);
((GeneralPath)shape).curveTo(107.58799, 332.70596, 126.63899, 352.94797, 159.04399, 352.94797);
((GeneralPath)shape).curveTo(169.991, 352.94797, 182.14099, 350.56998, 189.28499, 347.70996);
((GeneralPath)shape).lineTo(194.76799, 376.05597);
((GeneralPath)shape).curveTo(188.096, 379.39597, 173.094, 382.97498, 153.56, 382.97498);
((GeneralPath)shape).curveTo(98.06, 382.97598, 69.474, 348.42398, 69.474, 302.692);
((GeneralPath)shape).closePath();
((GeneralPath)shape).moveTo(451.534, 520.962);
((GeneralPath)shape).lineTo(96.75699, 520.962);
((GeneralPath)shape).lineTo(96.75699, 417.39197);
((GeneralPath)shape).lineTo(451.534, 417.39197);
((GeneralPath)shape).lineTo(451.534, 520.962);
((GeneralPath)shape).closePath();
((GeneralPath)shape).moveTo(427.518, 380.58298);
((GeneralPath)shape).lineTo(385.11902, 380.58298);
((GeneralPath)shape).lineTo(333.669, 220.04698);
((GeneralPath)shape).lineTo(373.456, 220.04698);
((GeneralPath)shape).lineTo(392.982, 287.94098);
((GeneralPath)shape).curveTo(398.461, 306.98697, 403.461, 325.32697, 407.281, 345.33798);
((GeneralPath)shape).lineTo(407.99002, 345.33798);
((GeneralPath)shape).curveTo(412.03802, 326.03998, 417.03503, 306.986, 422.51602, 288.645);
((GeneralPath)shape).lineTo(443.00302, 220.047);
((GeneralPath)shape).lineTo(481.60202, 220.047);
((GeneralPath)shape).lineTo(427.518, 380.583);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
origAlpha = alpha__0_0_0;
g.setTransform(defaultTransform__0_0_0);
g.setClip(clip__0_0_0);
origAlpha = alpha__0_0;
g.setTransform(defaultTransform__0_0);
g.setClip(clip__0_0);
float alpha__0_1 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_1 = g.getClip();
AffineTransform defaultTransform__0_1 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, -484.291015625f));
// _0_1 is CompositeGraphicsNode
origAlpha = alpha__0_1;
g.setTransform(defaultTransform__0_1);
g.setClip(clip__0_1);
float alpha__0_2 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_2 = g.getClip();
AffineTransform defaultTransform__0_2 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, -484.291015625f));
// _0_2 is CompositeGraphicsNode
origAlpha = alpha__0_2;
g.setTransform(defaultTransform__0_2);
g.setClip(clip__0_2);
float alpha__0_3 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_3 = g.getClip();
AffineTransform defaultTransform__0_3 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, -484.291015625f));
// _0_3 is CompositeGraphicsNode
origAlpha = alpha__0_3;
g.setTransform(defaultTransform__0_3);
g.setClip(clip__0_3);
float alpha__0_4 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_4 = g.getClip();
AffineTransform defaultTransform__0_4 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, -484.291015625f));
// _0_4 is CompositeGraphicsNode
origAlpha = alpha__0_4;
g.setTransform(defaultTransform__0_4);
g.setClip(clip__0_4);
float alpha__0_5 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_5 = g.getClip();
AffineTransform defaultTransform__0_5 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, -484.291015625f));
// _0_5 is CompositeGraphicsNode
origAlpha = alpha__0_5;
g.setTransform(defaultTransform__0_5);
g.setClip(clip__0_5);
float alpha__0_6 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_6 = g.getClip();
AffineTransform defaultTransform__0_6 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, -484.291015625f));
// _0_6 is CompositeGraphicsNode
origAlpha = alpha__0_6;
g.setTransform(defaultTransform__0_6);
g.setClip(clip__0_6);
float alpha__0_7 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_7 = g.getClip();
AffineTransform defaultTransform__0_7 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, -484.291015625f));
// _0_7 is CompositeGraphicsNode
origAlpha = alpha__0_7;
g.setTransform(defaultTransform__0_7);
g.setClip(clip__0_7);
float alpha__0_8 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_8 = g.getClip();
AffineTransform defaultTransform__0_8 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, -484.291015625f));
// _0_8 is CompositeGraphicsNode
origAlpha = alpha__0_8;
g.setTransform(defaultTransform__0_8);
g.setClip(clip__0_8);
float alpha__0_9 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_9 = g.getClip();
AffineTransform defaultTransform__0_9 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, -484.291015625f));
// _0_9 is CompositeGraphicsNode
origAlpha = alpha__0_9;
g.setTransform(defaultTransform__0_9);
g.setClip(clip__0_9);
float alpha__0_10 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_10 = g.getClip();
AffineTransform defaultTransform__0_10 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, -484.291015625f));
// _0_10 is CompositeGraphicsNode
origAlpha = alpha__0_10;
g.setTransform(defaultTransform__0_10);
g.setClip(clip__0_10);
float alpha__0_11 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_11 = g.getClip();
AffineTransform defaultTransform__0_11 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, -484.291015625f));
// _0_11 is CompositeGraphicsNode
origAlpha = alpha__0_11;
g.setTransform(defaultTransform__0_11);
g.setClip(clip__0_11);
float alpha__0_12 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_12 = g.getClip();
AffineTransform defaultTransform__0_12 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, -484.291015625f));
// _0_12 is CompositeGraphicsNode
origAlpha = alpha__0_12;
g.setTransform(defaultTransform__0_12);
g.setClip(clip__0_12);
float alpha__0_13 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_13 = g.getClip();
AffineTransform defaultTransform__0_13 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, -484.291015625f));
// _0_13 is CompositeGraphicsNode
origAlpha = alpha__0_13;
g.setTransform(defaultTransform__0_13);
g.setClip(clip__0_13);
float alpha__0_14 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_14 = g.getClip();
AffineTransform defaultTransform__0_14 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, -484.291015625f));
// _0_14 is CompositeGraphicsNode
origAlpha = alpha__0_14;
g.setTransform(defaultTransform__0_14);
g.setClip(clip__0_14);
float alpha__0_15 = origAlpha;
origAlpha = origAlpha * 1.0f;
g.setComposite(AlphaComposite.getInstance(3, origAlpha));
Shape clip__0_15 = g.getClip();
AffineTransform defaultTransform__0_15 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, -484.291015625f));
// _0_15 is CompositeGraphicsNode
origAlpha = alpha__0_15;
g.setTransform(defaultTransform__0_15);
g.setClip(clip__0_15);
origAlpha = alpha__0;
g.setTransform(defaultTransform__0);
g.setClip(clip__0);
g.setTransform(defaultTransform_);
g.setClip(clip_);

	}
	
	public Image getImage() {
		BufferedImage image =
            new BufferedImage(getIconWidth(), getIconHeight(),
                    BufferedImage.TYPE_INT_ARGB);
    	Graphics2D g = image.createGraphics();
    	paintIcon(null, g, 0, 0);
    	g.dispose();
    	return image;
	}

    /**
     * Returns the X of the bounding box of the original SVG image.
     * 
     * @return The X of the bounding box of the original SVG image.
     */
    public static int getOrigX() {
        return 6;
    }

    /**
     * Returns the Y of the bounding box of the original SVG image.
     * 
     * @return The Y of the bounding box of the original SVG image.
     */
    public static int getOrigY() {
        return 2;
    }

	/**
	 * Returns the width of the bounding box of the original SVG image.
	 * 
	 * @return The width of the bounding box of the original SVG image.
	 */
	public static int getOrigWidth() {
		return 64;
	}

	/**
	 * Returns the height of the bounding box of the original SVG image.
	 * 
	 * @return The height of the bounding box of the original SVG image.
	 */
	public static int getOrigHeight() {
		return 64;
	}

	/**
	 * The current width of this resizable icon.
	 */
	int width;

	/**
	 * The current height of this resizable icon.
	 */
	int height;
	
	/**
	 * Should this icon be drawn in a disabled state
	 */
	boolean disabled = false;

	/**
	 * Creates a new transcoded SVG image.
	 */
	public CSVFileIcon() {
        this(getOrigWidth(),getOrigHeight(),false);
	}
	
	public CSVFileIcon(boolean disabled) {
        this(getOrigWidth(),getOrigHeight(),disabled);
	}
	
	/**
	 * Creates a new transcoded SVG image with the given dimensions.
	 *
	 * @param size the dimensions of the icon
	 */
	public CSVFileIcon(Dimension size) {
		this(size.width, size.height, false);
	}
	
	public CSVFileIcon(Dimension size, boolean disabled) {
		this(size.width, size.height, disabled);
	}

	public CSVFileIcon(int width, int height) {
		this(width, height, false);
	}
	
	public CSVFileIcon(int width, int height, boolean disabled) {
		this.width = width;
		this.height = height;
		this.disabled = disabled;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.Icon#getIconHeight()
	 */
    @Override
	public int getIconHeight() {
		return height;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.Icon#getIconWidth()
	 */
    @Override
	public int getIconWidth() {
		return width;
	}

	public void setDimension(Dimension newDimension) {
		this.width = newDimension.width;
		this.height = newDimension.height;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.Icon#paintIcon(java.awt.Component, java.awt.Graphics,
	 * int, int)
	 */
    @Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.translate(x, y);
						
		Area clip = new Area(new Rectangle(0, 0, this.width, this.height));		
		if (g2d.getClip() != null) clip.intersect(new Area(g2d.getClip()));		
		g2d.setClip(clip);

		double coef1 = (double) this.width / (double) getOrigWidth();
		double coef2 = (double) this.height / (double) getOrigHeight();
		double coef = Math.min(coef1, coef2);
		g2d.scale(coef, coef);
		paint(g2d, disabled);
		g2d.dispose();
	}
}

