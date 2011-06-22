/*******************************************************************************
 * Copyright (c) 2010 Nicolas Roduit.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Nicolas Roduit - initial API and implementation
 ******************************************************************************/
package org.weasis.core.ui.graphic;

import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Arc2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.weasis.core.api.gui.util.GeomUtil;
import org.weasis.core.api.image.measure.MeasurementsAdapter;
import org.weasis.core.api.media.data.ImageElement;
import org.weasis.core.ui.util.MouseEventDouble;

/**
 * @author Nicolas Roduit,Benoit Jacquemoud
 */
public class AngleToolGraphic extends AbstractDragGraphic {

    public static final Icon ICON = new ImageIcon(AngleToolGraphic.class.getResource("/icon/22x22/draw-angle.png")); //$NON-NLS-1$

    public final static Measurement Angle = new Measurement("Angle", true);
    public final static Measurement ComplementaryAngle = new Measurement("Compl. Angle", true);

    // /////////////////////////////////////////////////////////////////////////////////////////////////////
    Point2D A, O, B; // Let AOB be the triangle that represents the measured angle, O being the intersection point

    boolean lineColinear; // estimate if OA & OB line segments are colinear not not
    boolean OAvalid, OBvalid; // estimate if line segments are valid or not

    double angleDeg; // smallest angle in Degrees in the range of [-180 ; 180] between OA & OB line segments

    // /////////////////////////////////////////////////////////////////////////////////////////////////////

    public AngleToolGraphic(float lineThickness, Color paintColor, boolean labelVisible) {
        super(3, paintColor, lineThickness, labelVisible);
        init();
    }

    @Override
    public Icon getIcon() {
        return ICON;
    }

    @Override
    public String getUIName() {
        return "Angle measure";
    }

    @Override
    protected void updateShapeOnDrawing(MouseEventDouble mouseEvent) {
        updateTool();

        Shape newShape = null;
        Path2D path = new Path2D.Double(Path2D.WIND_NON_ZERO, 2);

        if (OAvalid)
            path.append(new Line2D.Double(A, O), false);

        if (OBvalid)
            path.append(new Line2D.Double(O, B), false);

        if (OAvalid && OBvalid && !lineColinear) {
            AdvancedShape aShape = (AdvancedShape) (newShape = new AdvancedShape(2));
            aShape.addShape(path);

            // Let arcAngle be the partial section of the ellipse that represents the measured angle
            double startingAngle = GeomUtil.getAngleDeg(O, A);

            double radius = 32;
            Rectangle2D arcAngleBounds =
                new Rectangle2D.Double(O.getX() - radius, O.getY() - radius, 2 * radius, 2 * radius);

            Shape arcAngle = new Arc2D.Double(arcAngleBounds, startingAngle, angleDeg, Arc2D.OPEN);

            double rMax = Math.min(O.distance(A), O.distance(B)) * 2 / 3;
            double scalingMin = radius / rMax;

            aShape.addInvShape(arcAngle, O, scalingMin, true);

        } else if (path.getCurrentPoint() != null)
            newShape = path;

        setShape(newShape, mouseEvent);
        updateLabel(mouseEvent, getDefaultView2d(mouseEvent));
    }

    @Override
    public List<MeasureItem> getMeasurements(ImageElement imageElement, boolean releaseEvent, boolean drawOnLabel) {

        if (imageElement != null && isShapeValid()) {
            MeasurementsAdapter adapter = imageElement.getMeasurementAdapter();

            if (adapter != null) {
                ArrayList<MeasureItem> measVal = new ArrayList<MeasureItem>();

                if (Angle.isComputed() || ComplementaryAngle.isComputed()) {

                    double positiveAngle = Math.abs(angleDeg);

                    if (Angle.isComputed() && (!drawOnLabel || Angle.isGraphicLabel()))
                        measVal.add(new MeasureItem(Angle, positiveAngle, "deg"));

                    if (ComplementaryAngle.isComputed() && (!drawOnLabel || ComplementaryAngle.isGraphicLabel()))
                        measVal.add(new MeasureItem(ComplementaryAngle, 180.0 - positiveAngle, "deg"));
                }
                return measVal;
            }
        }
        return null;
    }

    @Override
    public boolean isShapeValid() {
        updateTool();
        return OAvalid && OBvalid;
    }

    // /////////////////////////////////////////////////////////////////////////////////////////////////////

    protected void init() {
        A = handlePointList.size() >= 1 ? handlePointList.get(0) : null;
        O = handlePointList.size() >= 2 ? handlePointList.get(1) : null;
        B = handlePointList.size() >= 3 ? handlePointList.get(2) : null;

        lineColinear = false;
        OAvalid = OBvalid = false;

        angleDeg = 0.0;
    }

    protected void updateTool() {
        init();

        OAvalid = (A != null && O != null && !O.equals(A));
        OBvalid = (B != null && O != null && !O.equals(B));

        if (OAvalid && OBvalid) {
            angleDeg = GeomUtil.getSmallestRotationAngleDeg(GeomUtil.getAngleDeg(A, O, B));
            lineColinear = GeomUtil.lineColinear(O, A, O, B);
        }
    }
}
