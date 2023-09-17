export interface User {
    uid: string,
    accessToken: string
}

export interface UserState {
    user: User | null,
    loggedIn: boolean
}
