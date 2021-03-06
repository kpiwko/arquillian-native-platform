/*
 * JBoss, Home of Professional Open Source
 * Copyright 2014, Red Hat Middleware LLC, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.arquillian.spacelift.task.archive;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

import java.io.File;

import org.arquillian.spacelift.Spacelift;
import org.arquillian.spacelift.task.archive.UntarTool;
import org.arquillian.spacelift.task.archive.UnzipTool;
import org.junit.Assert;
import org.junit.Test;

public class UncompressToolTest {

    @Test
    public void extractZipFile() {
        File helloExtracted = Spacelift.task(new File("src/test/resources/hello.zip"), UnzipTool.class)
            .toDir("target/hellozip")
            .execute()
            .await();
        Assert.assertThat(helloExtracted, notNullValue());
        Assert.assertThat(helloExtracted.exists(), is(true));
    }

    @Test
    public void extractTarGzFile() {
        File helloExtracted = Spacelift.task(new File("src/test/resources/hello.tgz"), UntarTool.class)
            .toDir("target/hellotgz")
            .execute()
            .await();
        Assert.assertThat(helloExtracted, notNullValue());
        Assert.assertThat(helloExtracted.exists(), is(true));
    }

    @Test
    public void extractTarBzip2File() {
        File helloExtracted = Spacelift.task(new File("src/test/resources/hello.tbz"), UntarTool.class)
            .bzip2(true)
            .toDir("target/hellotbz")
            .execute()
            .await();
        Assert.assertThat(helloExtracted, notNullValue());
        Assert.assertThat(helloExtracted.exists(), is(true));
    }

    @Test
    public void extractZipNested() {
        File helloExtracted = Spacelift.task(new File("src/test/resources/nested.zip"), UnzipTool.class)
            .toDir("target/nested0")
            .execute()
            .await();

        File present = new File(helloExtracted, "zipfolder");
        Assert.assertThat(present, notNullValue());
        Assert.assertThat(present.exists(), is(true));
    }

    @Test
    public void extractZipWithTextRemap() {
        File helloExtracted = Spacelift.task(new File("src/test/resources/nested.zip"), UnzipTool.class)
            .remap("zipfolder/").with("")
            .toDir("target/nested1")
            .execute()
            .await();

        File present = new File(helloExtracted, "bar");
        Assert.assertThat(present, notNullValue());
        Assert.assertThat(present.exists(), is(true));
    }

    @Test
    public void extractZipWithDoubleTextRemap() {
        File helloExtracted = Spacelift.task(new File("src/test/resources/nested.zip"), UnzipTool.class)
            .remap("zipfolder/").with("")
            .remap("bar/").with("foobar/")
            .toDir("target/nested2")
            .execute()
            .await();

        File present = new File(helloExtracted, "foobar");
        Assert.assertThat(present, notNullValue());
        Assert.assertThat(present.exists(), is(true));
    }

    @Test
    public void extractZipWithCutRemap() {
        File helloExtracted = Spacelift.task(new File("src/test/resources/nested.zip"), UnzipTool.class)
            .remap("^/?([^/]+)/(.*)").with("$2")
            .toDir("target/nested3")
            .execute()
            .await();

        File present = new File(helloExtracted, "bar");
        Assert.assertThat(present, notNullValue());
        Assert.assertThat(present.exists(), is(true));
    }

    @Test
    public void extractZipWithCutRemapTwice() {
        File helloExtracted = Spacelift.task(new File("src/test/resources/nested.zip"), UnzipTool.class)
            .remap("^/?([^/]+)/(.*)").with("$2")
            .remap("^/?([^/]+)/(.*)").with("$2")
            .toDir("target/nested4")
            .execute()
            .await();

        File present = new File(helloExtracted, "baz");
        Assert.assertThat(present, notNullValue());
        Assert.assertThat(present.exists(), is(true));

        present = new File(helloExtracted, "foo");
        Assert.assertThat(present, notNullValue());
        Assert.assertThat(present.exists(), is(true));
    }

    @Test
    public void extractZipWithCutdirsTwice() {
        File helloExtracted = Spacelift.task(new File("src/test/resources/nested.zip"), UnzipTool.class)
            .cutdirs()
            .cutdirs()
            .toDir("target/nested5")
            .execute()
            .await();

        File present = new File(helloExtracted, "baz");
        Assert.assertThat(present, notNullValue());
        Assert.assertThat(present.exists(), is(true));

        present = new File(helloExtracted, "foo");
        Assert.assertThat(present, notNullValue());
        Assert.assertThat(present.exists(), is(true));
    }

}
