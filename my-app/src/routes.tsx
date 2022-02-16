import { RouteObject } from "react-router-dom";
import Home from "./components/Home";
import DefaultLayout from "./components/containers/DefaultLayout";


const routes: RouteObject[] = [
    {
      path: "/",
      element: <DefaultLayout/>,
      children: [
        { index: true, element: <Home/> },
        { path: "*", element: <span>Home Page other</span> }
      ]
    }
  ];
  export default routes;