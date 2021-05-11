package org.atepe.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.atepe.model.Convenio;
import org.atepe.model.Role;
import org.atepe.model.Usuario;
import org.atepe.repository.RoleRepository;
import org.atepe.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UsuarioController {

	@Autowired
	private UsuarioRepository ur;

	@Autowired
	private RoleRepository rr;

	@GetMapping(value = "/cadastrarUsuario")
	public String Form(Usuario usuario, Model model) {
		Iterable<Usuario> usuarios = ur.findAll();
		Iterable<Role> roles = rr.findAll();
		model.addAttribute("lista_roles", roles);
		model.addAttribute("lista_Usuarios", usuarios);
		return "usuario/formUsuario";

	}

	@GetMapping(value = "/cancelarUsuario")
	public String Cancelar(Usuario usuario) {
		return "redirect:/cadastrarUsuario";
	}

	@RequestMapping(value = "/salvarUsuario", method = RequestMethod.POST)
	public ModelAndView salvarUsuario(@Valid Usuario usuario,@RequestParam("ativo") Boolean ativo, BindingResult bindingResult,
			RedirectAttributes atributes) {
		ModelAndView mv = new ModelAndView("/usuario/formUsuario");
		List<String> msg = new ArrayList<String>();
		if (bindingResult.hasErrors()) {
			Iterable<Usuario> usuarios = ur.findAll();
			mv.addObject("lista_Usuarios", usuarios);
			for (ObjectError objectErro : bindingResult.getAllErrors()) {
				msg.add(objectErro.getDefaultMessage());
			}
			mv.addObject("msg", msg);

			return mv;
		}
				
		Usuario usEntity = new Usuario();
		usEntity.setCodigo(usuario.getCodigo());
		usEntity.setNome(usuario.getNome().toUpperCase());
		usEntity.setEmail(usuario.getEmail());
		usEntity.setAtivo(ativo);		
		usEntity.setLogin(usuario.getLogin());
		/* CRIPTOGRAMA E INFORMA A SENHA */
		usEntity.setSenha(new BCryptPasswordEncoder().encode(usuario.getSenha()));
		ur.save(usEntity);

		Iterable<Usuario> usuarios = ur.findAll();
		mv.addObject("lista_Usuarios", usuarios);
		mv.addObject("usuario", new Usuario());
		msg.add("Registro salvo com sucesso...");
		mv.addObject("msg", msg);
		return mv;

	}

	@PostMapping("**/pesquisarUsuarios")
	public ModelAndView pesquisar(@RequestParam("nomepesquisa") String nomepesquisa, Convenio associado) {
		ModelAndView modelAndView = new ModelAndView("usuario/formUsuario");
		modelAndView.addObject("lista_Usuarios", ur.findByNome(nomepesquisa.toUpperCase()));
		modelAndView.addObject("usuario", new Usuario());
		return modelAndView;

	}

	@RequestMapping("/usuarios")
	public ModelAndView ListaUsuarios(Usuario usuario) {
		ModelAndView mv = new ModelAndView("/usuario/listaUsuario");
		Iterable<Usuario> usuarios = ur.findAll();
		mv.addObject("usuarios", usuarios);
		return mv;
	}

	@RequestMapping("/usuarios/{codigo}")
	public ModelAndView detalhesUsuario(@PathVariable("codigo") Long codigo) {

		Usuario usuario = ur.findByCodigo(codigo);
		ModelAndView mv = new ModelAndView("/usuario/detalhesUsuario");
		mv.addObject("usuario", usuario);
		return mv;
	}

	@RequestMapping("/deletarUsuario")
	public String deletarUsuario(Long codigo) {
		Usuario usuario = ur.findByCodigo(codigo);
		ur.delete(usuario);
		return "redirect:/cadastrarUsuario";
	}

	@GetMapping("/editarUsuario/{codigo}")
	public String editarUsuario(@PathVariable("codigo") Long codigo, Model model) {
		Usuario usu = ur.findByCodigo(codigo);
		model.addAttribute("usuario", usu);
		Iterable<Usuario> usuarios = ur.findAll();
		model.addAttribute("lista_Usuarios", usuarios);
		model.addAttribute("ativo", usu.isAtivo());
		return "usuario/formUsuario";
	}

}
