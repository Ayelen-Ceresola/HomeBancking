let { createApp } = Vue

createApp({
    data() {
        return {
            email: "",
            password: "",
            firstNameRegister: "",
            lastNameRegister: "",
            emailRegister: "",
            passwordRegister: "",
        }
    },

    created() {


    },


    methods: {
        login() {
            if (this.email && this.password) {
                axios.post("/api/login", `email=${this.email}&password=${this.password}`,
                    { headers: { 'content-type': 'application/x-www-form-urlencoded' } })

                    .then((res) => {
                        console.log('signed in!!!')
                        window.location.href = "/web/pages/accounts.html"
                        alert("Welcome")

                    })
                    .catch(err => console.log(err))

            } else {
                alert("Fill in all the fields")
            }

        },

        register() {
            if (this.emailRegister && this.passwordRegister && this.emailRegister && this.passwordRegister) {
                axios.post("/api/clients", `firstName=${this.firstNameRegister}&lastName=${this.lastNameRegister}&email=${this.emailRegister}&password=${this.passwordRegister}`,
                    { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
                    .then((res) => {
                        axios.post("/api/login", `email=${this.emailRegister}&password=${this.passwordRegister}`,
                            { headers: { 'content-type': 'application/x-www-form-urlencoded' } })

                            .then((res) => {
                                console.log('signed in!!!')
                                window.location.href = "/web/pages/accounts.html"
                                alert("Welcome")

                            })
                            .catch(err => console.log(err))

                    })
                    .catch(err => console.log(err))
            }else {
                alert("Fill in all the fields")
            }

        }
    }

}).mount("#app")


//axios.post('/api/login',"email=melba@mindhub.com&password=melba",{headers:{'content-type':'application/x-www-form-urlencoded'}}).then(response => console.log('signed in!!!'))

