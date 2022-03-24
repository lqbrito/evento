package com.evento.evento.controllers;

import com.evento.evento.models.Convidado;
import com.evento.evento.models.Evento;
import com.evento.evento.repositories.ConvidadoRepository;
import com.evento.evento.repositories.EventoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class EventoController {

    @Autowired
    private EventoRepository er;

    @Autowired
    private ConvidadoRepository cr;

    @RequestMapping(value="/cadastrarEvento", method= RequestMethod.GET)
    public String form(){
        return "evento/formEvento";
    }

    @RequestMapping(value="/cadastrarEvento", method= RequestMethod.POST)
    public String form(@Valid Evento evento, BindingResult result, RedirectAttributes attributes){
        if (result.hasErrors()){
            attributes.addFlashAttribute("flashMessage", "Não pode haver campos nulos!");
            attributes.addFlashAttribute("flashType", "danger");
            return "redirect:/eventos";
        }
        er.save(evento);
        attributes.addFlashAttribute("flashMessage", "Evento cadastrado com sucesso!");
        attributes.addFlashAttribute("flashType", "success");
        return "redirect:/eventos";
    }

    @RequestMapping(value="/eventos")
    public ModelAndView listaEventos(Evento evento){
        ModelAndView mv = new ModelAndView("evento/index");
        Iterable<Evento> eventos = er.findAll();
        mv.addObject("eventos", eventos);
        return mv;
    }

    @RequestMapping("/excluirEvento")
    public String excluirEvento(long codigo) {
    	Evento evento = er.findByCodigo(codigo);
    	er.delete(evento);
    	return "redirect:/eventos";
    }
    
    @RequestMapping("/excluirConvidado")
    public String excluirConvidado(String rg) {
    	Convidado convidado = cr.findByRg(rg);
    	cr.delete(convidado);
    	Evento evento = convidado.getEvento();
    	long codigoLong = evento.getCodigo();
    	String codigo = "" + codigoLong;
    	return "redirect:/" + codigo;
    }
    
    @RequestMapping(value="/{codigo}", method=RequestMethod.GET)
    public ModelAndView detalhesEvento(@PathVariable("codigo") long codigo) {
        Evento evento = er.findByCodigo(codigo);
        ModelAndView mv = new ModelAndView("evento/detalhesEvento");
        mv.addObject("evento", evento);
        Iterable<Convidado> convidados = cr.findByEvento(evento);
        mv.addObject("convidados", convidados);
        return mv;
    }

    @RequestMapping(value="/{codigo}", method=RequestMethod.POST)
    public String detalhesEventoPost(@PathVariable("codigo") long codigo, @Valid Convidado convidado, BindingResult result, RedirectAttributes attributes) {
        if (result.hasErrors()){
            attributes.addFlashAttribute("flashMessage", "Não pode haver campos nulos!");
            attributes.addFlashAttribute("flashType", "danger");
            return "redirect:/{codigo}";
        }
        Evento evento = er.findByCodigo(codigo);
        convidado.setEvento(evento);
        cr.save(convidado);
        attributes.addFlashAttribute("flashMessage", "Convidado cadastrado com sucesso!");
        attributes.addFlashAttribute("flashType", "success");
        return "redirect:/{codigo}";
    }

}
