/**
 * Copyright (C) 2015 Open WhisperSystems
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.whispersystems.claserver;

import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.google.common.collect.ImmutableList;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

class AuthFormController {
  static class FormInput {
    public Boolean twoCol;
    public String name;
    public String placeholder;
    public String html;

    FormInput() {
    }

    FormInput(Boolean twoCol, String name, String placeholder) {
      this.twoCol = twoCol;
      this.name = name;
      this.placeholder = placeholder;
    }
  }

  static class FormItem {
    public String labelFor;
    public String label;
    public boolean req;
    public List<FormInput> inputs;

    FormItem(String labelFor, String label, boolean req, List<FormInput> inputs) {
      this.labelFor = labelFor;
      this.label = label;
      this.req = req;
      this.inputs = inputs;
    }
  }

  public List<FormItem> items;
  public String baseUrl;

  AuthFormController(List<FormItem> items) {
    this.items = items;
    Config config = Config.getInstance();
    baseUrl = config.baseUrl;
  }

  public List<FormItem> items() {
    return items;
  }

  public static AuthFormController createEmptyFormController(MustacheFactory mf) throws IOException {
    FormInput countryInput = new FormInput();
    Mustache mustache = mf.compile("countries.mustache");
    StringWriter writer = new StringWriter();
    mustache.execute(writer, new Object()).flush();
    countryInput.html = writer.toString();
    countryInput.name = "country";
    countryInput.twoCol = true;

    ImmutableList<FormItem> items = ImmutableList.of(
            new FormItem("firstName", "Full Name", true, ImmutableList.of(
                    new FormInput(true, "firstName", "First Name"),
                    new FormInput(true, "lastName", "Last Name"))),
            new FormItem("email", "Email", true, ImmutableList.of(
                    new FormInput(false, "email", ""))),
            new FormItem("address1", "Mailing Address", true, ImmutableList.of(
                    new FormInput(false, "address1", "Street address"))),
            new FormItem(null, null, true, ImmutableList.of(
                    new FormInput(false, "address2", "Address line 2"))),
            new FormItem(null, null, false, ImmutableList.of(
                    new FormInput(true, "city", "City"),
                    new FormInput(true, "state", "State / Province / Region"))),
            new FormItem(null, null, true, ImmutableList.of(
                    new FormInput(true, "zip", "Postal / Zip Code"),
                    countryInput)),
            new FormItem("phone", "Phone Number", true, ImmutableList.of(
                    new FormInput(false, "phone", ""))),
            new FormItem("signature", "Electronic Signature: Type \"I AGREE\" to accept the terms above", true,
                    ImmutableList.of(new FormInput(false, "signature", "I AGREE")))
    );
    return new AuthFormController(items);
  }
}
