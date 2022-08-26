import { addDecorator } from "@storybook/react";
import { MemoryRouter } from "react-router-dom";
import "../src/App.css";
    
addDecorator(story => <MemoryRouter>{story()}</MemoryRouter>);

export const parameters = {
  actions: { argTypesRegex: "^on[A-Z].*" },
  controls: {
    matchers: {
      color: /(background|color)$/i,
      date: /Date$/,
    },
  },
}