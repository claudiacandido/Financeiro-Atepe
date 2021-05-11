 function del(codigo) {  
    if (confirm('Excluir a categoria?')) {  
			alert('deu certoooooooo');
        location.href = '@{/deletarAssociado/}' + codigo;
    }
}
