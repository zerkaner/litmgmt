import * as React from 'react';

type Action =
    | { type: 'setContext', username: string }
    | { type: 'isAuthenticated', value: boolean }

type Dispatch = (action: Action) => void
type State = {
    username: string,
    token: string,
    isAuthenticated: boolean,
}
type AuthProviderProps = { children: React.ReactNode }

const AuthStateContext = React.createContext<State | undefined>(undefined)
const AuthDispatchContext = React.createContext<Dispatch | undefined>(
    undefined,
)

function AuthReducer(state: State, action: Action): State {
    switch (action.type) {
        case 'setContext': {
            return { ...state, username: action.username}
        }
        case 'isAuthenticated': {
            return {...state, isAuthenticated: action.value}
        }
        default: {
            throw new Error(`Unhandled action type`)
        }
    }
}

function AuthProvider({ children }: AuthProviderProps) {
    const [state, dispatch] = React.useReducer(AuthReducer, { username: "anonymous", token: "", isAuthenticated: false })
    return (
        <AuthStateContext.Provider value={state}>
            <AuthDispatchContext.Provider value={dispatch}>
                {children}
            </AuthDispatchContext.Provider>
        </AuthStateContext.Provider>
    )
}

function useAuthState() {
    const context = React.useContext(AuthStateContext)
    if (context === undefined) {
        throw new Error('useAuthState must be used within a AuthProvider')
    }
    return context
}

function useAuthDispatch() {
    const context = React.useContext(AuthDispatchContext)
    if (context === undefined) {
        throw new Error('useAuthDispatch must be used within a AuthProvider')
    }
    return context
}

export {useAuthState, useAuthDispatch, AuthProvider}