import * as React from 'react';
import { Redirect, Route, RouteProps } from 'react-router-dom'
import { useAuthState } from '../contexts/authContext';

interface PrivateRouteProps extends RouteProps {
    component: any
  }

  export const PrivateRoute: React.FC<PrivateRouteProps> = ({component: Component, ...rest}) => {
      const global = useAuthState();

      return (
          <Route
          render={props => (global.isAuthenticated === true ? <Component {...props} /> : <Redirect to="/login" />)}
          {...rest}
        />
      )
  }