/*******************************************************************************
 * Copyright (c) 2015 Weasis Team.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Nicolas Roduit - initial API and implementation
 ******************************************************************************/
package org.weasis.jpeg;

import javax.imageio.spi.ImageReaderSpi;

import org.weasis.jpeg.internal.CharlsCodec;

public class NativeJLSImageReader extends NativeJPEGImageReader {

    NativeJLSImageReader(ImageReaderSpi originatingProvider) {
        super(originatingProvider);
    }

    @Override
    protected CharlsCodec getCodec() {
        return new CharlsCodec();
    }
}
