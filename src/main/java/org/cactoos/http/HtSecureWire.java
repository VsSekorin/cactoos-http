/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2018 Yegor Bugayenko
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.cactoos.http;

import java.io.IOException;
import java.net.Socket;
import java.net.URI;
import javax.net.ssl.SSLSocketFactory;
import org.cactoos.BiFunc;
import org.cactoos.Input;
import org.cactoos.scalar.Constant;

/**
 * Wire that supports https.
 *
 * @author Vedran Vatavuk (123vgv@gmail.com)
 * @version $Id$
 * @since 0.1
 */
public final class HtSecureWire implements Wire {
    /**
     * Address.
     */
    private final String address;

    /**
     * TCP port.
     */
    private final int port;

    /**
     * Socket.
     */
    private final BiFunc<String, Integer, Socket> socket;

    /**
     * Ctor.
     * @param uri The address of the server
     */
    public HtSecureWire(final URI uri) {
        this(uri.getHost(), uri.getPort());
    }

    /**
     * Ctor.
     * @param addr The address of the server
     */
    public HtSecureWire(final String addr) {
        // @checkstyle MagicNumber (1 line)
        this(addr, 443);
    }

    /**
     * Ctor.
     * @param addr The address of the server
     * @param tcp The TCP port
     */
    public HtSecureWire(final String addr, final int tcp) {
        this(
            addr,
            tcp,
            (host, prt) -> SSLSocketFactory.getDefault().createSocket(host, prt)
        );
    }

    /**
     * Ctor.
     * @param addr The address of the server
     * @param tcp The TCP port
     * @param sck Ssl socket
     */
    public HtSecureWire(final String addr,
        final int tcp, final BiFunc<String, Integer, Socket> sck) {
        this.address = addr;
        this.port = tcp;
        this.socket = sck;
    }

    @Override
    public Input send(final Input input) throws IOException {
        return new HtWire(this.address, new Constant<>(this.port), this.socket)
            .send(input);
    }
}