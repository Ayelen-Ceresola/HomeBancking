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
                if (this.email.includes("admin")) {
                    axios.post("/api/login", `email=${this.email}&password=${this.password}`,
                        { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
                        .then(res => {
                            if (res.status == 200) {
                                Swal.fire({
                                    position: 'center',
                                    icon: 'success',
                                    title: 'Welcome!',
                                    showConfirmButton: false,
                                    timer: 1500
                                })
                                setTimeout(() => {
                                    window.location.href = "/web/manager.html";
                                }, 1800)
                            }
                        }).catch(err => {
                            Swal.fire({
                                position: 'center',
                                icon: 'error',
                                title: 'Incorrect, try again!!',
                                showConfirmButton: false,
                                timer: 1500
                            })
                        })
                } else {
                    axios.post('/api/login', `email=${this.email}&password=${this.password}`, { headers: { 'content-type': 'application/x-www-form-urlencoded' }})
                        .then( res => {{
                                Swal.fire({
                                    position: 'center',
                                    icon: 'success',
                                    title: 'Welcome!',
                                    showConfirmButton: false,
                                    timer: 1000
                                })
                                setTimeout(() => {
                                    window.location.href = "/web/pages/accounts.html";
                                }, 1800)
                            }
                        }).catch(err => {
                            Swal.fire({
                                position: 'center',
                                icon: 'error',
                                title: 'Incorrect, try again!!',
                                showConfirmButton: false,
                                timer: 1500
                            })
                        })
                }
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
                                Swal.fire({
                                    position: 'top-center',
                                    icon: 'success',
                                    title: 'Welcome',
                                    showConfirmButton: false,
                                    timer: 2000,
                                    customClass:{
                                        popup: `alertCss`
                                    }
                                })

                            })
                            .catch(err => console.log(err))

                    })
                    .catch(err => console.log(err))
            }else {
                Swal.fire({
                    position: 'top-center',
                    icon: 'error',
                    title: 'Fill in all the fields',
                    showConfirmButton: false,
                    timer: 2000,
                    customClass:{
                        popup: `alertCss`
                    }
                })
                
                
            }

        }
    }

}).mount("#app")


//axios.post('/api/login',"email=melba@mindhub.com&password=melba",{headers:{'content-type':'application/x-www-form-urlencoded'}}).then(response => console.log('signed in!!!'))

