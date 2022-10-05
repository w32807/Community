class JwtToken{
    accessToken: string;
    refreshToken: string;

    constructor(accessToken: string, refreshToken: string){
        this.accessToken = 'Bearer ' + accessToken;
        this.refreshToken = 'Bearer ' + refreshToken;
    }
}

export default JwtToken